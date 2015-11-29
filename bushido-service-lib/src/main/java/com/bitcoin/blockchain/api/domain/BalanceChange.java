package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2015-03-03.
 */
public class BalanceChange implements Serializable {

    private Balance balance;

    private List<FCBalance> FCBalances;

    public Balance getBalance() {
        return this.balance;
    }

    public List<FCBalance> getFCBalances() {
        return FCBalances;
    }

    private void setFCBalances(List<FCBalance> balances) {
        this.FCBalances = balances;
    }

    public BalanceChange() {

    }

    public BalanceChange(Balance balance) {
        this.balance = balance;
    }

    public BalanceChange(Balance balance, List<FCBalance> FCBalances) {
        this.balance = balance;
        this.FCBalances = FCBalances;
    }

    public BalanceChange(List<FCBalance> FCBalances) {
        this.FCBalances = FCBalances;
    }
}
