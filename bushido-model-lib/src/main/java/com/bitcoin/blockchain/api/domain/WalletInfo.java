package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2014-12-29.
 */
public class WalletInfo implements Serializable {

    public String key;

    public String currentAddress;

    public Balance balance;

    public List<FCBalance> fcBalances;

    public WalletInfo(String key, String currentAddress, Balance balance) {
        this.key = key;
        this.currentAddress = currentAddress;
        this.balance = balance;
    }

    public WalletInfo(String key, String currentAddress, Balance balance, List<FCBalance> fcBalances) {
        this.key = key;
        this.currentAddress = currentAddress;
        this.balance = balance;
        this.fcBalances = fcBalances;
    }

    public WalletInfo() {

    }

    public WalletInfo clone() {
        return new WalletInfo(this.key, this.currentAddress, this.balance.clone());
    }
}
