package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-01-21.
 */
public class V2WalletSetting implements Serializable {

    public static String PASSPHRASE = "passphrase";
    public static String COMPRESSED_KEYS = "compressedKeys";
    public static String INSTRUMENTS = "instruments";

    public String key;

    public Object value;

    public V2WalletSetting(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public V2WalletSetting() {

    }

    public V2WalletSetting clone() {
        return new V2WalletSetting(this.key, this.value);
    }
}
