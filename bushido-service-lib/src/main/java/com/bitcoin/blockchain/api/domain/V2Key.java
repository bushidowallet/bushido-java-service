package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-01-21.
 */
public class V2Key implements Serializable {

    private String walletKey;

    public String publicKey;
    //Hex representation of a public key byte[]
    public String publicKeyBytes;
    public String address;
    public int account;
    public int sequence;
    public long creationTimeMillis;

    public V2Key(String walletKey,
                 String publicKey,
                 String address,
                 int account,
                 int sequence,
                 long creationTimeMillis) {
        this.walletKey = walletKey;
        this.publicKey = publicKey;
        this.address = address;
        this.account = account;
        this.sequence = sequence;
        this.creationTimeMillis = creationTimeMillis;
    }

    public V2Key(String walletKey,
                 String publicKey,
                 String publicKeyBytes,
                 String address,
                 int account,
                 int sequence,
                 long creationTimeMillis) {
        this(walletKey, publicKey, address, account, sequence, creationTimeMillis);
        this.publicKeyBytes = publicKeyBytes;
    }

    public V2Key() {

    }

    public String getWalletKey() {
        return walletKey;
    }
}
