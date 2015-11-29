package com.bitcoin.blockchain.api.service.v2wallet;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-01-09.
 */
public class V2WalletRegistry {

    @Autowired
    public V2WalletMessageRouter router;

    private List<V2Wallet> wallets;

    public V2WalletRegistry() {
        this.wallets = new ArrayList<V2Wallet>();
    }

    public List<V2Wallet> getWallets() {
        return wallets;
    }

    public boolean hasWallet(String key) {
        for (V2Wallet wallet : wallets) {
            if (wallet.getDescriptor(false).getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public V2Wallet getWallet(String key) {
        for (V2Wallet wallet : wallets) {
            if (wallet.getDescriptor(false).getKey().equals(key)) {
                return wallet;
            }
        }
        return null;
    }

    public void addWallet(V2Wallet wallet) {
        wallets.add(wallet);
        router.register(wallet.getDescriptor(false).getKey());
    }

    public void removeWallet(V2Wallet wallet) {
        wallets.remove(wallet);
    }
}
