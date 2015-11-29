package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.core.bip32.Hash;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.Error;
import com.bitcoin.blockchain.api.domain.message.ClientMessageBase;
import com.bitcoin.blockchain.api.domain.message.IMessage;
import com.bitcoin.blockchain.api.persistence.TopUpTransactionDAO;
import com.bitcoin.blockchain.api.persistence.UserDAO;
import com.bitcoin.blockchain.api.persistence.UserDAOImpl;
import com.bitcoin.blockchain.api.persistence.V2WalletDAO;
import com.bitcoin.blockchain.api.service.transaction.TransactionService;
import com.bitcoin.blockchain.api.util.AccountUtil;
import com.bitcoin.blockchain.api.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jesion on 2015-01-09.
 */
@Service
public class V2WalletServiceImpl implements V2WalletService, ApplicationContextAware {

    @Autowired
    public V2WalletRegistry wallets;

    @Autowired
    public V2WalletDAO walletDAO;

    @Autowired
    public UserDAO userDAO;

    @Autowired
    public TopUpTransactionDAO topUpTransactionDAO;

    @Autowired
    public TransactionService transactionService;

    public V2WalletRegistry getWallets() {
        return this.wallets;
    }

    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    public UserLoginResponse create(V2WalletDescriptor wallet) {
        UserLoginResponse operation = new UserLoginResponse();
        if (wallet.hasAccount() == false) {
            wallet.accounts.add(new V2WalletAccount(0, "Default Account"));
        }
        if (walletDAO.hasWallet(wallet.key) == true) {
            operation.addError(new Error(1));
        } else {
            try {
                V2WalletSetting passphrase = wallet.getSetting("passphrase");
                passphrase.value = ByteUtil.toHex(new Hash((String) passphrase.value).hash());
                walletDAO.create(new PersistedV2WalletDescriptor(wallet));
                List<PersistedV2WalletDescriptor> persistedWallets = walletDAO.getUserWallets(wallet.owner);
                List<V2WalletDescriptor> wallets = new ArrayList<V2WalletDescriptor>();
                for (int i = 0; i < persistedWallets.size(); i++) {
                    V2WalletDescriptor userwallet = persistedWallets.get(i).toBase();
                    V2Wallet w = getWallet(userwallet.key);
                    wallets.add(w.getDescriptor(true));
                }
                operation.setWallets(wallets);
                operation.user = userDAO.get(wallet.owner).toBase();
            } catch (Exception e) {
                operation.addError(new Error(14));
            }
        }
        return operation;
    }

    public Response addAccount(String key, V2WalletAccount account) {
        final Response operation = new Response();
        final V2WalletDescriptor wallet = walletDAO.get(key);
        if (wallet != null) {
            if (wallet.hasAccountByName(account.name) == false) {
                account.account = AccountUtil.getNextAccount(wallet);
                wallet.accounts.add(account);
                walletDAO.updateAccounts(key, wallet.accounts);
                final V2Wallet w = wallets.getWallet(wallet.key);
                if (w != null) {
                    final V2WalletDescriptor walletDescriptor = w.getDescriptor(false);
                    walletDescriptor.accounts.add(account);
                }
                operation.setPayload(wallet);
            } else {
                operation.addError(new Error(13));
            }
        } else {
            operation.addError(new Error(12));
        }
        return operation;
    }

    public Response delete(V2WalletDescriptor wallet) {
        Response operation = new Response();
        if (walletDAO.hasWallet(wallet.key) != true) {
            operation.addError(new Error(3));
        } else {
            walletDAO.delete(wallet.key);
            operation.setPayload(true);
        }
        return operation;
    }

    public List<V2WalletDescriptor> getAll() {
        List<PersistedV2WalletDescriptor> all = walletDAO.getAll();
        List<V2WalletDescriptor> allBase = new ArrayList<V2WalletDescriptor>();
        for (PersistedV2WalletDescriptor descriptor : all) {
            allBase.add(descriptor);
        }
        return allBase;
    }

    public V2WalletDescriptor get(String key) {
        return walletDAO.get(key);
    }

    public V2Wallet getWallet(String key) {
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            return wallet;
        }
        return null;
    }

    /**
     * Gets public keys with transactions associated with them
     * @param key
     * @param account
     * @return
     */
    public Response getTransactionsKeys(String key, int account) {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            operation.setPayload(wallet.getTransactionsKeys(account));
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    public Response getKeys(String key, int account) {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            operation.setPayload(wallet.getKeys(account));
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    public Response getTransactions(String key, int account) {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            operation.setPayload(wallet.getTransactions(account));
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    public Response getSpendingTransactions(String key) {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            operation.setPayload(wallet.getSpendingTransactions());
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    public Response spendAllUTXO(String key, String address) throws Exception {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            operation.setPayload(wallet.spendAllUTXO(address));
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    public Response spendAllUTXOPush(String key, String address) throws Exception {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            String txHex = (String) wallet.spendAllUTXO(address).getPayload();
            transactionService.push(txHex);
            operation.setPayload(true);
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    public Response getUnspentOutputs(String key) {
        Response operation = new Response();
        if (walletDAO.hasWallet(key)) {
            V2Wallet wallet = wallets.getWallet(key);
            if (wallet == null) {
                wallet = init(key);
            }
            operation.setPayload(wallet.getUnspent());
        } else {
            operation.addError(new Error(3));
        }
        return operation;
    }

    private LoginResponse login(String key, String username, String password) {
        LoginResponse operation = new LoginResponse();
        if (checkCredentials(username, password) == true) {
            if (walletDAO.isValid(new PersistedV2WalletDescriptor(key, username)) == true) {
                final V2Wallet wallet = getWallet(key);
                operation.setPayload(wallet.getDescriptor(true));
            } else {
                operation.addError(new Error(6));
            }
        } else {
            operation.addError(new Error(15));
        }
        return operation;
    }

    public LoginResponse login(V2WalletDescriptor wallet, String password) {
        return login(wallet.key, wallet.owner, password);
    }

    /*
    From client
     */
    public void messageToWallet(ClientMessageBase incoming) {
        if (walletDAO.hasWallet(incoming.getKey())) {
            V2Wallet wallet = wallets.getWallet(incoming.getKey());
            if (wallet == null) {
                wallet = init(incoming.getKey());
            }
            wallet.messageTo((IMessage) incoming);
        }
    }

    private V2Wallet init(String key) {
        V2Wallet wallet = context.getBean("v2Wallet", V2Wallet.class);
        wallet.setDescriptor(get(key));
        wallet.init();
        wallets.addWallet(wallet);
        return wallet;
    }

    private boolean checkCredentials(String username, String password) {
        final UserDAOImpl.UserInfo user = userDAO.isValid(username, password);
        return user.valid;
    }

    public String initTopUp(V2WalletDescriptor wallet) {
        final PersistedTopUpTransaction tx = new PersistedTopUpTransaction();
        tx.status = TopUpTransactionStatus.INITIALIZED;
        tx.walletKey = wallet.getKey();
        tx.userId = wallet.owner;
        tx.control = UUID.randomUUID().toString();
        topUpTransactionDAO.create(tx);
        return tx.control;
    }

    public void handleTopUpNotification(TopUpNotification notification) {
        final PersistedTopUpTransaction tx = topUpTransactionDAO.getByControl(notification.control);
        if (tx != null) {
            if (notification.status.equals(TopUpTransactionStatus.COMPLETED)) {
                if (walletDAO.hasWallet(tx.walletKey)) {
                    V2Wallet wallet = wallets.getWallet(tx.walletKey);
                    if (wallet == null) {
                        wallet = init(tx.walletKey);
                    }
                    wallet.handleTopUpComplete(tx, notification);
                }
            }
        }
    }
}
