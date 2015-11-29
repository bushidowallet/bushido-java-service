package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-02-17.
 *
 * This class represents a spent output indicator
 */
public class TransactionOutpoint implements Serializable {

    public String walletKey;

    //indicates tx that has spendable outputs
    public String txHash;

    public long index;

    public TransactionOutpoint(String walletKey, String txHash, long index) {
        this(txHash, index);
        this.walletKey = walletKey;
    }

    public TransactionOutpoint(String txHash, long index) {
        this.txHash = txHash;
        this.index = index;
    }

    public TransactionOutpoint() {

    }

    @Override
    public String toString() {
        return "Tx hash: " + txHash + " index: " + index + " walletkey: " + walletKey;
    }
}
