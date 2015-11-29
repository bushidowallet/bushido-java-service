package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedUser;
import com.bitcoin.blockchain.api.domain.PersistedVerificationToken;
import com.bitcoin.blockchain.api.domain.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jesion on 2015-11-12.
 */
@Service("verificationTokenDAO")
public class VerificationTokenDAOImpl implements VerificationTokenDAO {

    public static String TOKEN_COLLECTION = "token";

    @Autowired
    public MongoOperations mongoOps;

    public PersistedVerificationToken getActiveToken(PersistedUser user, VerificationToken.VerificationTokenType type) {
        final Query query = new Query(Criteria.where("type").is(type).and("userId").is(user.getId()).and("secondFactorRequired").is(user.has2FAEnabled));
        final List<PersistedVerificationToken> tokens = this.mongoOps.find(query, PersistedVerificationToken.class, TOKEN_COLLECTION);
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).hasExpired() == false) {
                return tokens.get(i);
            }
        }
        return null;
    }

    public void secondFactorComplete(PersistedVerificationToken t) {
        final Query query = new Query(Criteria.where("id").is(t.getId()));
        final Update update = new Update();
        update.set("secondFactorComplete", true);
        this.mongoOps.upsert(query, update, PersistedVerificationToken.class, TOKEN_COLLECTION);
    }

    public PersistedVerificationToken get(String token) {
        final Query query = new Query(Criteria.where("token").is(token));
        return this.mongoOps.findOne(query, PersistedVerificationToken.class, TOKEN_COLLECTION);
    }

    public void create(PersistedVerificationToken t) {
        this.mongoOps.insert(t, TOKEN_COLLECTION);
    }
}
