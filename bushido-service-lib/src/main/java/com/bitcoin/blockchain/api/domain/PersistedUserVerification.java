package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Jesion on 2015-11-17.
 */
@Document
public class PersistedUserVerification extends UserVerification {

    @Id
    private String id;

    public PersistedUserVerification() {
        super();
    }

    public PersistedUserVerification(String userId, List<String> verifications) {
        super(userId, verifications);
    }

    public String getId() {
        return id;
    }
}
