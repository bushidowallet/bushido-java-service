package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2014-11-26.
 */
public class Wallet implements Serializable {

    public Wallet(String key) {
        setKey(key);
    }

    public Wallet() {

    }

    private String key;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String currentAddress;

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    private long creationTime;

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    private Balance balance;

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Balance getBalance() {
        return this.balance;
    }

    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
