package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2014-12-19.
 */
public class TransactionStatus implements Serializable {

    public static String CONFIRMED = "Confirmed";
    public static String PENDING = "Pending";

    public String txHash;

    //list of IPs
    public List<String> broadcasters;

    //depth in chain
    public int confirmations;

    public long blockHeight;

    public String blockHash;

    public String status;

    public TransactionStatus(String txHash,
                             List<String> broadcasters,
                             int confirmations,
                             long blockHeight,
                             String blockHash,
                             String status) {
        this.txHash = txHash;
        this.broadcasters = broadcasters;
        this.confirmations = confirmations;
        this.blockHeight = blockHeight;
        this.blockHash = blockHash;
        this.status = status;
    }

    public TransactionStatus() {

    }

    public boolean isNotPending() {
        return status.equals(PENDING) == false;
    }
}
