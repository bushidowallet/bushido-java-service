package com.bitcoin.blockchain.api.domain;

import java.util.List;

/**
 * Created by Jesion on 2015-06-02.
 */
public class RegV2WalletDescriptor extends V2WalletDescriptor {

    public String regCode;

    public RegV2WalletDescriptor(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts,
                              String regCode) {
        super(key, owner, settings, accounts, null);
        this.regCode = regCode;
    }

    public RegV2WalletDescriptor() {

    }
}
