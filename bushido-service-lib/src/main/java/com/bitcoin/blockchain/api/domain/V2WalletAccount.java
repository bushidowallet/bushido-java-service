package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-01-21.
 */
public class V2WalletAccount implements Serializable {

    public int account;
    public String name;

    public V2WalletAccount(int account, String name) {
        this.account = account;
        this.name = name;
    }

    public V2WalletAccount() {

    }

    public V2WalletAccount clone() {
        return new V2WalletAccount(account, name);
    }
}
