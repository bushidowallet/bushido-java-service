package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Jesion on 2015-02-26.
 */
@Document
public class PersistedV2Key extends V2Key {

    @Id
    private String id;

    public PersistedV2Key(String walletKey,
                          String publicKey,
                          String address,
                          int account,
                          int sequence,
                          long creationTimeMillis) {
        super(walletKey,
            publicKey,
            address,
            account,
            sequence,
            creationTimeMillis
        );
    }

    public PersistedV2Key(String walletKey,
                          String publicKey,
                          String publicKeyBytes,
                          String address,
                          int account,
                          int sequence,
                          long creationTimeMillis) {
        super(walletKey,
                publicKey,
                publicKeyBytes,
                address,
                account,
                sequence,
                creationTimeMillis
        );
    }

    public PersistedV2Key() {

    }

    public String getId() {
        return id;
    }

    public V2Key toBase() {
        return new V2Key(this.getWalletKey(),
            this.publicKey,
            this.publicKeyBytes,
            this.address,
            this.account,
            this.sequence,
            this.creationTimeMillis
        );
    }
}
