package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedUserVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-11-17.
 */
@Service("userVerificationDAO")
public class UserVerificationDAOImpl implements UserVerificationDAO {

    public static String VERIFICATION_COLLECTION = "userverification";

    @Autowired
    public MongoOperations mongoOps;

    public PersistedUserVerification get(String userId) {
        final Query query = new Query(Criteria.where("userId").is(userId));
        return this.mongoOps.findOne(query, PersistedUserVerification.class, VERIFICATION_COLLECTION);
    }

    public void add(String userId, String verification) {
        final Query query = new Query(Criteria.where("userId").is(userId));
        PersistedUserVerification uv = this.mongoOps.findOne(query, PersistedUserVerification.class, VERIFICATION_COLLECTION);
        if (uv != null) {
            if (uv.hasVerification(verification) == false) {
                final List<String> newVerifications = new ArrayList<String>();
                newVerifications.addAll(uv.verifications);
                newVerifications.add(verification);
                final Update u = new Update();
                u.set("verifications", newVerifications);
                this.mongoOps.upsert(query, u, PersistedUserVerification.class, VERIFICATION_COLLECTION);
            }
        } else {
            final List<String> uver = new ArrayList<String>();
            uver.add(verification);
            uv = new PersistedUserVerification(userId, uver);
            this.mongoOps.insert(uv, VERIFICATION_COLLECTION);
        }
    }

    public boolean isVerified(String userId, String verification) {
        final Query query = new Query(Criteria.where("userId").is(userId));
        final PersistedUserVerification uv = this.mongoOps.findOne(query, PersistedUserVerification.class, VERIFICATION_COLLECTION);
        if (uv != null) {
            if (uv.hasVerification(verification) == true) {
                return true;
            }
        }
        return false;
    }
}
