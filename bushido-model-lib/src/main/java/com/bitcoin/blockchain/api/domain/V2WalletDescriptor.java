package com.bitcoin.blockchain.api.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-01-20.
 */
public class V2WalletDescriptor extends V2WalletDescriptorBase {

    public V2WalletDescriptor(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts) {
        this(key, owner, settings, accounts, null);
    }

    public V2WalletDescriptor(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts,
                              WalletInfo info) {
        super(key, owner, settings, accounts, info);
    }

    public V2WalletDescriptor(String key, String owner) {
        super(key, owner);
    }

    public V2WalletDescriptor() {
        super();
    }

    public V2WalletDescriptor clone() {
        V2WalletDescriptor c = new V2WalletDescriptor(key, owner);
        c.settings = new ArrayList<V2WalletSetting>();
        c.accounts = new ArrayList<V2WalletAccount>();
        if (this.info != null) {
            c.info = this.info.clone();
        }
        for (int i = 0; i < settings.size(); i++) {
            c.settings.add(settings.get(i).clone());
        }
        for (int j = 0; j < accounts.size(); j++) {
            c.accounts.add(accounts.get(j).clone());
        }
        return c;
    }
}
