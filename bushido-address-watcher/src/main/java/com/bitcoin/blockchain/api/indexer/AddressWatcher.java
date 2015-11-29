package com.bitcoin.blockchain.api.indexer;

import com.bitcoin.blockchain.api.application.Environment;
import com.bitcoin.blockchain.api.Command;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.domain.message.IMessage;
import com.bitcoin.blockchain.api.domain.message.TransactionMessage;
import com.bitcoin.blockchain.api.domain.message.TransactionStatusMessage;
import com.bitcoin.blockchain.api.util.TransactionUtil;
import com.bitcoin.blockchain.api.application.ApplicationConfig;
import org.bitcoinj.core.AbstractWalletEventListener;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.script.Script;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.System;
import java.util.List;

/**
 * Created by Jesion on 2014-11-27.
 */
public class AddressWatcher {

    @Autowired
    public ApplicationConfig config;

    @Autowired
    public MessageSender sender;

    private Wallet wallet;

    public AddressWatcher() {
        this.wallet = new Wallet("globalAddressWatcher");
    }

    public void setWalletDTO(Wallet wallet) {
        this.wallet = wallet;
    }

    private WalletAppKit kit;

    @PostConstruct
    public void init() {
        NetworkParameters params = config.getNetworkParams();
        String home = System.getProperty("user.home");
        File file = new File(home + "/.");
        kit = new WalletAppKit(params, file, wallet.getKey());
        try {
            System.out.println("Blockchain indexer file: " + file.getCanonicalPath());
        } catch (Exception e) {

        }
        if(config.getEnvironment().equals(Environment.REGTEST)) {
            kit.connectToLocalHost();
        }
        kit.startAsync();
        kit.awaitRunning();
        WalletListener wListener = new WalletListener();
        kit.wallet().addEventListener(wListener);
    }

    public void addWatchingAddress(V2Key key) {
        try {
            kit.wallet().addWatchedAddress(new Address(this.config.getNetworkParams(), key.address.toString()), key.creationTimeMillis / 1000);
        } catch (Exception e) {
            System.out.println("Could not register address: " + key.address.toString());
        }
        System.out.println("Now watching address: " + key.address);
    }

    public void addWatchingMultisigAddress(String address) {
        try {
            kit.wallet().addWatchedAddress(new Address(this.config.getNetworkParams(), address), System.currentTimeMillis() / 1000);
        } catch (Exception e) {
            System.out.println("Could not register multisig address: " + address);
        }
        System.out.println("Now watching multisig address: " + address);
    }

    private void messageAPI(String correlationId, String command, Object payload) {
        IMessage outMessage = null;
        if (command.equals(Command.BALANCE_CHANGE_RECEIVED) || command.equals(Command.BALANCE_CHANGE_SPENT)) {
            final Transaction tx = (Transaction) payload;
            outMessage = new TransactionMessage(command, correlationId, tx);
        } else if (command.equals(Command.TRANSACTION_STATUS_CHANGE)) {
            final TransactionStatus status = (TransactionStatus) payload;
            outMessage = new TransactionStatusMessage(command, correlationId, status);
        }
        if (outMessage != null && sender != null) {
            sender.send(outMessage);
        }
    }

    public void dispose() {
        System.out.println("shutting down wallet");
        kit.stopAsync();
        kit.awaitTerminated();
        kit.stop();
    }

    private class WalletListener extends AbstractWalletEventListener {

        @Override
        public void onCoinsReceived(org.bitcoinj.core.Wallet wallet, org.bitcoinj.core.Transaction tx, Coin prevBalance, Coin newBalance) {
            System.out.println("-----> coins resceived: " + tx.getHashAsString());
            System.out.println("received: " + tx.getValue(wallet));
            String command = tx.getValue(wallet).getValue() > 0 ? Command.BALANCE_CHANGE_RECEIVED : Command.BALANCE_CHANGE_SPENT;
            System.out.println("TX onCoinsReceived: " + tx.toString() + " command: " + command);
            if (command == Command.BALANCE_CHANGE_RECEIVED) {
                messageAPI(null, command, new PersistedTransactionDTOBuilder(tx, new TransactionUtil(), config.getNetworkParams(), wallet).build());
            }
        }

        @Override
        public void onTransactionConfidenceChanged(org.bitcoinj.core.Wallet wallet, org.bitcoinj.core.Transaction tx) {
            System.out.println("-----> confidence changed: " + tx.getHashAsString());
            TransactionConfidence confidence = tx.getConfidence();
            System.out.println("new block depth: " + confidence.getDepthInBlocks());
            TransactionStatus status = new TransactionStatusDTOBuilder(tx.getHashAsString(), tx.getConfidence()).build();
            System.out.println("TX onTransactionConfidenceChanged: " + tx.toString());
            messageAPI(null, Command.TRANSACTION_STATUS_CHANGE, status);
        }

        @Override
        public void onCoinsSent(org.bitcoinj.core.Wallet wallet, org.bitcoinj.core.Transaction tx, Coin prevBalance, Coin newBalance) {
            System.out.println("-----> coins spent: " + tx.getHashAsString());
            System.out.println("spent: " + tx.getValue(wallet));
            String command = tx.getValue(wallet).getValue() > 0 ? Command.BALANCE_CHANGE_RECEIVED : Command.BALANCE_CHANGE_SPENT;
            System.out.println("TX onCoinsSent: " + tx.toString() + " command: " + command);
            messageAPI(null, command, new PersistedTransactionDTOBuilder(tx, new TransactionUtil(), config.getNetworkParams(), wallet).build());
        }

        @Override
        public void onReorganize(org.bitcoinj.core.Wallet wallet) {
        }

        @Override
        public void onWalletChanged(org.bitcoinj.core.Wallet wallet) {
        }

        @Override
        public void onKeysAdded(List<ECKey> keys) {
            System.out.println("new key added");
        }

        public void onScriptsChanged(org.bitcoinj.core.Wallet wallet, List<Script> scripts, boolean isAddingScripts) {
            System.out.println("new script added");
        }
    }
}
