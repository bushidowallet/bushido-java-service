package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2015-02-25.
 */
public class V2WalletDescriptorBase implements Serializable {
    public String key;

    //this is either user or organisation
    public String owner;

    public List<V2WalletSetting> settings;

    public List<V2WalletAccount> accounts;

    public WalletInfo info;

    public V2WalletDescriptorBase(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts) {
        this(key, owner, settings, accounts, null);
    }

    public V2WalletDescriptorBase(String key,
                              String owner,
                              List<V2WalletSetting> settings,
                              List<V2WalletAccount> accounts,
                              WalletInfo info) {
        this.key = key;
        this.owner = owner;
        this.settings = settings;
        this.accounts = accounts;
        this.info = info;
    }

    public V2WalletDescriptorBase(String key, String owner) {
        this.key = key;
        this.owner = owner;
    }

    public V2WalletDescriptorBase() {

    }

    public boolean hasAccount(int account) {
        for (V2WalletAccount accountDTO : accounts) {
            if (accountDTO.account == account) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAccountByName(String name) {
        for (V2WalletAccount accountDTO : accounts) {
            if (accountDTO.name == name) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAccount() {
        if (accounts == null) {
            return false;
        }
        for (V2WalletAccount accountDTO : accounts) {
            return true;
        }
        return false;
    }

    public V2WalletSetting getSetting(String key) {
        for (V2WalletSetting settingDTO : settings) {
            if (settingDTO.key.equals(key)) {
                return settingDTO;
            }
        }
        return null;
    }

    public String getKey() {
        return this.key;
    }
}
