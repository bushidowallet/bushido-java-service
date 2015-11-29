package com.bitcoin.blockchain.api.messaging;

import com.bitcoin.blockchain.api.Command;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.message.IMessage;
import com.bitcoin.blockchain.api.domain.message.TransactionMessage;
import com.bitcoin.blockchain.api.domain.message.TransactionStatusMessage;
import com.bitcoin.blockchain.api.persistence.TransactionDAO;
import com.bitcoin.blockchain.api.persistence.V2WalletKeyDAO;
import com.bitcoin.blockchain.api.service.v2wallet.V2Wallet;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletService;
import com.bitcoin.blockchain.api.util.TransactionUtil;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jesion on 2015-02-02.
 */
public class IndexerToWalletReceiver {

    @Autowired
    public V2WalletService wallets;

    @Autowired
    public V2WalletKeyDAO keyDAO;

    @Autowired
    public TransactionDAO transactionDAO;

    public void receive(IMessage message) {
        System.out.println("GOT message from indexer: " + message);
        if (message.getCommand().equals(Command.BALANCE_CHANGE_RECEIVED)) {
            newIncomingTransactionHandler(message);
        } else if (message.getCommand().equals(Command.BALANCE_CHANGE_SPENT)) {
            newOutgoingTransactionHandler(message);
        } else if (message.getCommand().equals(Command.TRANSACTION_STATUS_CHANGE)) {
            transactionStatusChangeHandler(message);
        }
    }

    @Autowired
    public Queue indexer2walletQueue;

    @Autowired
    public DirectExchange indexer2walletExchange;

    @Autowired
    public AmqpAdmin amqpAdmin;

    @PostConstruct
    public void init() {
        Binding b = BindingBuilder.bind(indexer2walletQueue).to(indexer2walletExchange).with("");
        amqpAdmin.declareBinding(b);
    }

    private void transactionStatusChangeHandler(IMessage message) {
        TransactionStatusMessage tsm = (TransactionStatusMessage) message;
        PersistedTransaction tx = transactionDAO.readByHash(tsm.getPayload().txHash);
        if (tx != null) {
            V2Key key = keyDAO.get(tx.keyId);
            if (key != null) {
                V2Wallet wallet = wallets.getWallet(key.getWalletKey());
                wallet.transactionStatusChangeHandler(tsm.getPayload(), tx);
            } else {
                //spending transaction status changed... no key is present on this transaction
                V2Wallet wallet = wallets.getWallet(tx.walletId);
                wallet.transactionStatusChangeHandler(tsm.getPayload(), tx);
            }
        } else {
            System.out.println("Logged an invalid transaction");
        }
    }

    private void newOutgoingTransactionHandler(IMessage message) {
        TransactionMessage tm = (TransactionMessage) message;
        PersistedTransaction tx = null;
        if (tm.getPayload() instanceof PersistedTransaction) {
            tx = (PersistedTransaction) tm.getPayload();
        } else {
            tx = new PersistedTransaction(tm.getPayload());
        }
        List<TransactionOutpoint> outpoints = new ArrayList<TransactionOutpoint>();
        List<String> hash = new ArrayList<String>();
        System.out.println("Money has been spent, transaction: " + tx.hash);
        for (int i = 0; i < tx.inputs.size(); i++) {
            TransactionOutpoint o = TransactionUtil.getOutpoint(tx.inputs.get(i));
            System.out.println("Outpoint: " + o.toString());
            outpoints.add(o);
            hash.add(tx.inputs.get(i).getOutpointTransactionHash());
        }
        List<PersistedTransaction> relatedTxs = transactionDAO.readByHash(hash);
        Map<String, List<TransactionOutput>> walletOutputs = new HashMap<String, List<TransactionOutput>>();
        for (int j = 0; j < relatedTxs.size(); j++) {
            Transaction relatedTx = relatedTxs.get(j);
            System.out.println("Related tx: " + relatedTx.hash + " wallet: " + relatedTx.walletId + " acc: " + relatedTx.account);
            if (walletOutputs.get(relatedTx.walletId) == null) {
                List<TransactionOutput> l = new ArrayList<TransactionOutput>();
                walletOutputs.put(relatedTx.walletId, l);
            }
            for (int k = 0; k < relatedTx.outputs.size(); k++) {
                TransactionOutput out = relatedTx.getOutput(k);
                walletOutputs.get(relatedTx.walletId).add(out);
                System.out.println("Related tx output: " + out.toString());
            }
        }
        for(Map.Entry<String, List<TransactionOutput>> entry : walletOutputs.entrySet()) {
            String walletId = entry.getKey();
            V2Wallet wallet = wallets.getWallet(walletId);
            wallet.newSpendingTransactionHandler(tx);
        }
    }

    private void newIncomingTransactionHandler(IMessage message) {
        Map<String, PersistedV2Key> keys = new HashMap<String, PersistedV2Key>();
        TransactionMessage tm = (TransactionMessage) message;
        System.out.println("Incoming funds detected: tx hash:" + tm.getPayload().hash);
        for (TransactionOutput output : tm.getPayload().outputs) {
            String outA = output.getToAddress();
            System.out.println("Checking address " + outA + " found on incoming output. Searching for keys rendered...");
            List<PersistedV2Key> k = keyDAO.find(output.getToAddress());
            if (k.size() == 1) {
                keys.put(k.get(0).getWalletKey(), k.get(0));
            } else if (keys.size() == 0) {
                //not found
            } else {
                //found many keys for a transaction...
                //this can be a result of the situation where 2 wallets are using the same passphrase
            }
        }
        if (keys.size() == 1) {
            for (Map.Entry<String, PersistedV2Key> entry : keys.entrySet()) {
                V2Wallet wallet = wallets.getWallet(entry.getKey());
                try {
                    PersistedTransaction t = null;
                    if (tm.getPayload() instanceof PersistedTransaction) {
                        t = (PersistedTransaction) tm.getPayload();
                    } else {
                        t = new PersistedTransaction(tm.getPayload());
                    }
                    wallet.newTransactionHandler(t, entry.getValue());
                } catch (Exception e) {
                    System.out.println("Error while handling new transaction: " + e);
                }
            }
        } else {
            //have not found a V2Key for this transaction..
        }
    }
}
