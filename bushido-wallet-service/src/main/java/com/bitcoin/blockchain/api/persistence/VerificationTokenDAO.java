package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedUser;
import com.bitcoin.blockchain.api.domain.PersistedVerificationToken;
import com.bitcoin.blockchain.api.domain.VerificationToken;

/**
 * Created by Jesion on 2015-11-12.
 */
public interface VerificationTokenDAO {

    public VerificationToken getActiveToken(PersistedUser user, VerificationToken.VerificationTokenType type);

    public void create(PersistedVerificationToken t);

    public PersistedVerificationToken get(String token);

    public void secondFactorComplete(PersistedVerificationToken t);
}
