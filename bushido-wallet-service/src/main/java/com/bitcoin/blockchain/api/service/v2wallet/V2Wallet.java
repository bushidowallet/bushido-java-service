package com.bitcoin.blockchain.api.service.v2wallet;

import com.bccapi.bitlib.StandardTransactionBuilder;
import com.bitcoin.blockchain.api.Command;
import com.bitcoin.blockchain.api.FiatCurrency;
import com.bitcoin.blockchain.api.FiatCurrencyGateway;
import com.bitcoin.blockchain.api.IndexerCommand;
import com.bitcoin.blockchain.api.builder.TransactionBuilder;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.Error;
import com.bitcoin.blockchain.api.domain.message.*;
import com.bitcoin.blockchain.api.messaging.WalletToIndexerSender;
import com.bitcoin.blockchain.api.persistence.TopUpTransactionDAO;
import com.bitcoin.blockchain.api.persistence.TransactionDAO;
import com.bitcoin.blockchain.api.persistence.TransactionOutpointDAO;
import com.bitcoin.blockchain.api.service.transaction.TransactionService;
import com.bitcoin.blockchain.api.service.user.UserPinRegistry;
import com.bitcoin.blockchain.api.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Jesion on 2015-01-09.
 */
public class V2Wallet implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static long STANDARD_FEE = 20000;

    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public V2WalletMessageRouter router;

    @Autowired
    public V2Keygen keygen;

    @Autowired
    public WalletToIndexerSender toIndexerSender;

    @Autowired
    public TransactionDAO transactionDAO;

    @Autowired
    public TransactionOutpointDAO outpointDAO;

    @Autowired
    public TransactionService transactionService;

    @Autowired
    public TopUpTransactionDAO topUpTransactionDAO;

    @Autowired
    public ExchangeModel model;

    @Autowired
    public UserPinRegistry pinRegistry;

    @Value("${app.pin.enabled}")
    private String pinEnabled;

    @Value("${app.pin.salt}")
    private String pinSalt;

    public void init(String pin) {
        keygen.setWallet(getDescriptor(false));
        log.info( "Initializing wallet: " + descriptor.key + " pin enabled: " + pinEnabled);
        try {
            keygen.init(pinEnabled, pinSalt, pin);
        } catch (Exception e) {
            System.out.println("Error while initializing Keygen...");
            log.error("Error initializing wallet: " + descriptor.key + " pin enabled: " + pinEnabled);
        }
    }

    private V2WalletDescriptor descriptor;

    public V2WalletDescriptor getDescriptor(boolean includeInfo) {
        if (includeInfo == true) {
            descriptor.info = getInfo();
        }
        return descriptor;
    }

    public void setDescriptor(V2WalletDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<PersistedV2Key> getTransactionsKeys(int account) {
        List<PersistedTransaction> transactions = getTransactions(account);
        List<String> keysIds = new ArrayList<String>();
        for (Transaction tx : transactions) {
            keysIds.add(tx.keyId);
        }
        return keygen.getKeys(keysIds);
    }

    public List<PersistedV2Key> getKeys(int account) {
        return keygen.getKeys(account);
    }

    public List<PersistedTransaction> getTransactions(int account) {
        return transactionDAO.getTransactions(this.descriptor.getKey(), account);
    }

    public List<PersistedTransaction> getSpendingTransactions() {
        return transactionDAO.getSpendingTransactions(this.descriptor.getKey());
    }

    public void messageTo(IMessage incoming) {
        Message out = null;
        String txHex = null;
        if (incoming.getCommand().equals(Command.GET_ADDRESS)) {
            out = new GetAddressMessage();
            out.setCommand(Command.GET_ADDRESS);
            out.setCorrelationId(incoming.getCorrelationId());
            try {
                int account = 0;
                if (incoming.getPayload() != null) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) incoming.getPayload()).entrySet())
                        if (entry.getKey().equals("account")) {
                            account = (Integer) entry.getValue();
                        }
                }
                final V2Key key = createKey(account);
                if (key != null) {
                    ((GetAddressMessage) out).setPayload(new WalletInfo(this.descriptor.key, key.address.toString(), getBalance(), getFCBalances(new String[]{FiatCurrency.PLN})));
                } else {
                    log.error("Keygen error while attempting to generate address. No PIN available in the registry (?), so can't rebuild the root key...");
                }
            } catch (Exception e) {
                log.error(e.toString());
            }
        } else if (incoming.getCommand().equals(Command.GET_INSTRUMENT_DATA)) {
            V2WalletSetting instruments = this.descriptor.getSetting(V2WalletSetting.INSTRUMENTS);
            if (instruments != null) {
                String[] instr = ((String) instruments.value).split(",");
                for (int j = 0; j < instr.length; j++) {
                    String instrument = instr[j];
                    ExchangeData data = null;
                    if (instrument.equals(Instruments.btcpln.id)) {
                        data = model.exchanges.get(Exchanges.bitmarket.id).data;
                    }
                    if (data != null) {
                        out = new ClientMessage();
                        out.setCommand(Command.GET_INSTRUMENT_DATA);
                        out.setCorrelationId(incoming.getCorrelationId());
                        ((ClientMessage) out).setPayload(data);
                    }
                }
            }
        } else if (incoming.getCommand().equals(Command.GET_UNSPENT_OUTPUTS)) {
            out = new UnspentOutputsMessage();
            out.setCommand(Command.GET_UNSPENT_OUTPUTS);
            out.setCorrelationId(incoming.getCorrelationId());
            ((UnspentOutputsMessage) out).setPayload(getUnspent());
        } else if (incoming.getCommand().equals(Command.SPEND_ALL_UTXO)) {
            try {
                txHex = (String) spendAllUTXO((String) incoming.getPayload()).getPayload();
            } catch (Exception e) {

            }
        } else if (incoming.getCommand().equals(Command.SPEND)) {
            try {
                txHex = (String) spend((List<SpendDescriptor>) incoming.getPayload()).getPayload();
            } catch (Exception e) {

            }
        }
        if (out != null) {
            router.sendUpdate(this.descriptor.key, out);
        }
        if (txHex != null) {
            transactionService.push(txHex);
        }
    }

    private WalletInfo getInfo() {
        return new WalletInfo(this.descriptor.key, null, getBalance(), getFCBalances(new String[]{FiatCurrency.PLN}));
    }

    /**
     * New transaction handler, incoming funds only based on BIP32 derived key
     *
     * @param tx - incoming funds transaction
     * @param key - related V2Key
     * @throws Exception
     */
    public void newTransactionHandler(PersistedTransaction tx, PersistedV2Key key) throws Exception {
        listenForUpdates(tx);
        tx.keyId = key.getId();
        tx.walletId = this.descriptor.getKey();
        tx.account = key.account;
        System.out.println("Incoming transaction captured in API service: " + tx.toString());
        transactionDAO.create(tx);
        WalletChange change = new WalletChange(tx.value, getBalance(), createKey(key.account).address, tx);
        WalletChangeMessage out = new WalletChangeMessage();
        out.setCommand(Command.BALANCE_CHANGE_RECEIVED);
        out.setKey(this.descriptor.key);
        out.setPayload(change);
        router.sendUpdate(this.descriptor.key, out);
    }

    /**
     * Method implementation incomplete and unused
     * @param spendings
     * @return
     * @throws Exception
     */
    public Response spend(List<SpendDescriptor> spendings) throws Exception {
        final boolean checkPin = Boolean.parseBoolean(pinEnabled);
        final UserPin pin = pinRegistry.get(this.descriptor.owner);
        final V2WalletSetting seed = this.descriptor.getSetting(V2WalletSetting.PASSPHRASE);
        final V2WalletSetting compressed = this.descriptor.getSetting(V2WalletSetting.COMPRESSED_KEYS);
        final Response response = new Response();
        final Map<String, TransactionOutputList> unspents = getUnspent();
        final Balance balance = new Balance(unspents.get(TransactionStatus.CONFIRMED).total, unspents.get(TransactionStatus.PENDING).total);
        long fee = STANDARD_FEE;
        final long toSpend = balance.confirmed + balance.unconfirmed - fee;
        if (balance.confirmed + balance.unconfirmed > 0) {
            //Receive change on 0 account
            final V2Key key = createKey(0);
            response.setPayload(new TransactionBuilder(unspents, balance, spendings, key, V2WalletCrypto.decrypt((String) seed.value, checkPin, pin.pin, pinSalt), (Boolean) compressed.value, fee).build());
        } else {
            response.addError(new Error(15));
        }
        return response;
    }

    /**
     * Alter the fee - it has to depend on Tx size..
     * @param address
     * @return
     * @throws Exception
     */
    public Response spendAllUTXO(String address) throws Exception {
        final boolean checkPin = Boolean.parseBoolean(pinEnabled);
        final UserPin pin = pinRegistry.get(this.descriptor.owner);
        final V2WalletSetting seed = this.descriptor.getSetting(V2WalletSetting.PASSPHRASE);
        final V2WalletSetting compressed = this.descriptor.getSetting(V2WalletSetting.COMPRESSED_KEYS);
        final Response response = new Response();
        final Map<String, TransactionOutputList> unspents = getUnspent();
        final Balance balance = new Balance(unspents.get(TransactionStatus.CONFIRMED).total, unspents.get(TransactionStatus.PENDING).total);
        long fee = STANDARD_FEE;
        long toSpend = balance.confirmed + balance.unconfirmed - fee;
        if (balance.confirmed + balance.unconfirmed > 0) {
            //Receive change on 0 account
            final V2Key key = createKey(0);
            List<SpendDescriptor> spendings = new ArrayList<SpendDescriptor>();
            spendings.add(new SpendDescriptor(address, toSpend));
            String tx = null;
            try {
                tx = new TransactionBuilder(unspents, balance, spendings, key, V2WalletCrypto.decrypt((String) seed.value, checkPin, pin.pin, pinSalt), (Boolean) compressed.value, fee).build();
            } catch (StandardTransactionBuilder.InsufficientFundsException exception) {

            }
            response.setPayload(tx);
        } else {
            response.addError(new Error(15));
        }
        return response;
    }

    public void newSpendingTransactionHandler(PersistedTransaction tx) {
        listenForUpdates(tx);
        List<TransactionOutpoint> outpoints = new ArrayList<TransactionOutpoint>();
        long totalInputs = 0;
        long totalOutputs = 0;
        long change = 0;
        long fee = 0;
        long balanceChange = 0;
        //see what are we spending...
        for (int i = 0; i < tx.inputs.size(); i++) {
            TransactionInput ti = tx.inputs.get(i);
            if (isMineIncomingTx(ti.getOutpointTransactionHash())) {
                TransactionOutpoint o = TransactionUtil.getOutpoint(ti);
                o.walletKey = this.descriptor.getKey();
                outpoints.add(o);
                Transaction t = transactionDAO.readByHash(o.txHash);
                long inputVal = t.getOutput((int) o.index).getValue();
                ti.setValue(inputVal);
                totalInputs += inputVal;
            }
        }
        outpointDAO.save(outpoints);
        //see if we are getting any change...
        for (int j = 0; j < tx.outputs.size(); j++) {
            TransactionOutput to = tx.outputs.get(j);
            totalOutputs += to.getValue();
            if (isChange(to)) {
                //yes it is a change so reflect it balance calculations...
                change += to.getValue();
            }
        }
        fee = totalInputs - totalOutputs;
        tx.walletId = this.descriptor.getKey();
        tx.fee = fee;
        balanceChange = totalOutputs + fee - change;
        tx.value = -1 * balanceChange;
        transactionDAO.create(tx);
        System.out.println("Outgoing transaction captured in API service " + tx.toString());
        System.out.println("Spending tx params: total inp: " + totalInputs + " total out: " + totalOutputs + " change: " + change + " fee: " + fee + " balanceChange: " + balanceChange);
        WalletChangeMessage out = new WalletChangeMessage();
        out.setCommand(Command.BALANCE_CHANGE_SPENT);
        out.setKey(this.descriptor.key);
        WalletChange walletChange = new WalletChange(-1 * balanceChange, getBalance(), null, tx);
        out.setPayload(walletChange);
        router.sendUpdate(this.descriptor.key, out);
    }

    private boolean isChange(TransactionOutput to) {
        List<PersistedV2Key> keys = keygen.getKeys(-1);
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).address.equals(to.getToAddress())) {
                return true;
            }
        }
        return false;
    }

    private boolean isMineIncomingTx(String hash) {
        final Transaction t = transactionDAO.readByHash(hash);
        return t.walletId.equals(this.descriptor.getKey());
    }

    public Map<String, TransactionOutputList> getUnspent() {
        Map<String, TransactionOutputList> unspents = new HashMap<String, TransactionOutputList>();
        unspents.put(TransactionStatus.PENDING, new TransactionOutputList(TransactionStatus.PENDING));
        unspents.put(TransactionStatus.CONFIRMED, new TransactionOutputList(TransactionStatus.CONFIRMED));
        List<PersistedTransaction> incoming = transactionDAO.getTransactions(this.descriptor.getKey());
        List<TransactionOutpoint> spentOutpoints = outpointDAO.list(this.descriptor.getKey());
        List<PersistedV2Key> keys = keygen.getKeys(-1);
        for (int i = 0; i < incoming.size(); i++) {
            Transaction tx = incoming.get(i);
            for (int j = 0; j < tx.outputs.size(); j++) {
                TransactionOutput output = tx.outputs.get(j);
                if (isMine(output, keys)) {
                    boolean spent = isSpent(tx.hash, Long.valueOf(j), spentOutpoints);
                    if (spent == false) {
                        TransactionOutputList l = unspents.get(tx.status);
                        l.total += output.getValue();
                        l.outputs.add(output);
                    }
                }
            }
        }
        return unspents;
    }

    public Balance getBalance() {
        final Map<String, TransactionOutputList> unspents = getUnspent();
        return new Balance(unspents.get(TransactionStatus.CONFIRMED).total, unspents.get(TransactionStatus.PENDING).total);
    }

    private boolean isMine(TransactionOutput output, List<PersistedV2Key> keys) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).address.equals(output.getToAddress())) {
                output.setKey(keys.get(i).toBase());
                return true;
            }
        }
        return false;
    }

    private boolean isSpent(String txHash, long outputIndex, List<TransactionOutpoint> spentOutpoints) {
        for (int i = 0; i < spentOutpoints.size(); i++) {
            TransactionOutpoint outpoint = spentOutpoints.get(i);
            if (outpoint.txHash.equals(txHash) && outpoint.index == outputIndex) {
                return true;
            }
        }
        return false;
    }

    public void transactionStatusChangeHandler(TransactionStatus status, PersistedTransaction tx) {
        transactionDAO.update(tx, status);
        ClientMessage out = new ClientMessage();
        out.setCommand(Command.TRANSACTION_STATUS_CHANGE);
        out.setPayload(status);
        router.sendUpdate(this.descriptor.key, out);
        if (tx.status.equals(TransactionStatus.PENDING) && status.isNotPending()) {
            balanceChanged();
        }
    }

    public void instrumentUpdate(Instrument instrument, ExchangeData data) {
        InstrumentChange change = new InstrumentChange(instrument, data);
        InstrumentChangeMessage out = new InstrumentChangeMessage();
        out.setCommand(Command.INSTRUMENT_CHANGE);
        out.setKey(this.descriptor.key);
        out.setPayload(change);
        System.out.println("This wallet has interest in " + instrument.id + " so dispatching INSTRUMENT_CHANGE: " + change.data.getBid() + " to wallet " + this.descriptor.key);
        router.sendUpdate(this.descriptor.key, out);
    }

    private void balanceChanged() {
        BalanceChange change = new BalanceChange(getBalance());
        BalanceChangeMessage out = new BalanceChangeMessage();
        out.setCommand(Command.BALANCE_CHANGE_STATUS);
        out.setKey(this.descriptor.key);
        out.setPayload(change);
        System.out.println("Funds went to confirmed state so dispatching BALANCE_CHANGE_STATUS: " + change.getBalance().toString() + " on wallet " + this.descriptor.key);
        router.sendUpdate(this.descriptor.key, out);
    }

    private V2Key createKey(int account) throws Exception {
        V2Key key = keygen.getKey(account);
        watch(key);
        return key;
    }

    private void watch(V2Key key) {
        final KeyMessage keyMessage = new KeyMessage(IndexerCommand.WATCH_KEY, "", key);
        toIndexerSender.send(keyMessage);
    }

    @Value("${app.onchainuser}")
    private String onchainuser;

    @Value("${app.onchainpass}")
    private String onchainpass;

    @Value("${app.chainapi}")
    private String chainapiUrl;

    public String listenForUpdates(PersistedTransaction tx) {
        ChainMessage message = new ChainMessage(tx.hash);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application","json"));
        requestHeaders.add("Authorization", getAuth(onchainuser, onchainpass));
        HttpEntity<ChainMessage> requestEntity = new HttpEntity<ChainMessage>(message, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(chainapiUrl, HttpMethod.POST, requestEntity, String.class);
            return responseEntity.getStatusCode().toString();
        } catch (Exception e) {

        }
        return null;
    }

    public void handleTopUpComplete(PersistedTopUpTransaction tx, TopUpNotification notification) {
        tx.status = TopUpTransactionStatus.COMPLETED;
        tx.amount = notification.amount;
        tx.date = notification.dateTime;
        tx.gateway = FiatCurrencyGateway.DOTPAY;
        tx.gatewayTransactionId = notification.id;
        tx.currency = notification.currency;
        tx.originalAmount = notification.originalAmount;
        tx.commission = notification.commission;
        tx.widthdrawalAmount = notification.widthdrawalAmount;
        topUpTransactionDAO.update(tx);
        notifyCBalance(tx.currency);
    }

    private String getAuth(final String u, final String p) {
        String auth = u + ":" + p;
        byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }

    private List<FCBalance> getFCBalances(String[] currencies) {
        BigDecimal cBalance = BigDecimal.ZERO;
        final List<PersistedTopUpTransaction> txs = topUpTransactionDAO.getByWalletAndCurrency(this.descriptor.getKey(), currencies[0]);
        for (int i = 0; i < txs.size();  i++) {
            if (txs.get(i).amount.longValue() > 0) {
                System.out.println("Adding: " + txs.get(i).amount );
                cBalance = cBalance.add(txs.get(i).amount);
            }
        }
        List<FCBalance> cb = new ArrayList<FCBalance>();
        cb.add(new FCBalance(currencies[0], cBalance));
        return cb;
    }

    private void notifyCBalance(String currency) {
        List<FCBalance> cb = getFCBalances( new String[]{ currency } );
        BalanceChange change = new BalanceChange(cb);
        BalanceChangeMessage out = new BalanceChangeMessage();
        out.setCommand(Command.FC_BALANCE_CHANGE_RECEIVED);
        out.setKey(this.descriptor.key);
        out.setPayload(change);
        System.out.println("Fiat currency funds appeared on this wallet so dispatching FC_BALANCE_CHANGE_RECEIVED");
        router.sendUpdate(this.descriptor.key, out);
    }

    @Value("${app.chainuser}")
    private String chainUser;

    @Value("${app.chainpass}")
    private String chainPass;

    @Value("${app.apiurl}")
    private String apiUrl;

    private class ChainMessage {

        public String type = "transaction";
        public String block_chain = "bitcoin";
        public String transaction_hash;
        public String url = "https://" + chainUser + ":" + chainPass + "@" + apiUrl + "/walletapi/api/v2/tx/notifytx";

        public ChainMessage() {

        }

        public ChainMessage(String txHash) {
            this.transaction_hash = txHash;
        }
    }
}
