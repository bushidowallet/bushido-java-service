package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedUserVerification;

/**
 * Created by Jesion on 2015-11-17.
 */
public interface UserVerificationDAO {

    public PersistedUserVerification get(String userId);

    public void add(String userId, String verification);

    public boolean isVerified(String userId, String verification);
}
