package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Jesion on 2015-02-26.
 */
@Document
public class PersistedV2WalletDescriptor extends V2WalletDescriptor {

    @Id
    private String id;

    public PersistedV2WalletDescriptor(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts) {
        super(key, owner, settings, accounts);
    }

    public PersistedV2WalletDescriptor(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts,
                              WalletInfo info) {
        super(key, owner, settings, accounts, info);
    }

    public PersistedV2WalletDescriptor(String key, String owner) {
        super(key, owner);
    }

    public PersistedV2WalletDescriptor() {
        super();
    }

    public PersistedV2WalletDescriptor(V2WalletDescriptor d) {
        this(d.key, d.owner, d.settings, d.accounts, d.info);
    }

    public V2WalletDescriptor toBase() {
        return new V2WalletDescriptor(this.key, this.owner, this.settings, this.accounts, this.info);
    }
}

