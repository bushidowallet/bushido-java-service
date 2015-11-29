package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Jesion on 2015-11-12.
 */
@Document
public class PersistedVerificationToken extends VerificationToken {

    @Id
    private String id;

    public PersistedVerificationToken() {
        super();
    }

    public PersistedVerificationToken(VerificationTokenType type,
                             Date creationDate,
                             Date expiryDate,
                             String token,
                             String userId,
                             boolean secondFactorRequired,
                             boolean secondFactorComplete) {
        super(type,
            creationDate,
            expiryDate,
            token,
            userId,
            secondFactorRequired,
            secondFactorComplete
        );
    }

    public PersistedVerificationToken(VerificationToken t) {
        super(t.type,
            t.creationDate,
            t.expiryDate,
            t.token,
            t.userId,
            t.secondFactorRequired,
            t.secondFactorComplete
        );
    }

    public String getId() {
        return id;
    }

    public VerificationToken toBase() {
        return new VerificationToken(this.type,
                this.creationDate,
                this.creationDate,
                this.token,
                this.userId,
                this.secondFactorRequired,
                this.secondFactorComplete
        );
    }
}
