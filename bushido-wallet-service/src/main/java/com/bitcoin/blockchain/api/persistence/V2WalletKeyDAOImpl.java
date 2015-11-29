package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedV2Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jesion on 2015-01-21.
 */
@Service("walletKeyDAO")
public class V2WalletKeyDAOImpl implements V2WalletKeyDAO {

    private static String KEY_COLLECTION = "key";

    @Autowired
    public MongoOperations mongoOps;

    public void logKey(PersistedV2Key key) {
        mongoOps.insert(key, KEY_COLLECTION);
    }

    public List<PersistedV2Key> getKeys(String walletKey, int account) {
        final Query query = new Query(Criteria.where("walletKey").is(walletKey));
        if (account >= 0) {
            query.addCriteria(Criteria.where("account").is(account));
        }
        return this.mongoOps.find(query, PersistedV2Key.class, KEY_COLLECTION);
    }

    public List<PersistedV2Key> getKeys(List<String> ids) {
        final Query query = new Query(Criteria.where("id").in(ids));
        return mongoOps.find(query, PersistedV2Key.class, KEY_COLLECTION);
    }

    public List<PersistedV2Key> find(String address) {
        final Query query = new Query(Criteria.where("address").is(address));
        return this.mongoOps.find(query, PersistedV2Key.class, KEY_COLLECTION);
    }

    public PersistedV2Key get(String id) {
        return this.mongoOps.findById(id, PersistedV2Key.class, KEY_COLLECTION);
    }

    public PersistedV2Key getKey(String walletKey, int account, int sequence) {
        final Query query = new Query(Criteria.where("walletKey").is(walletKey));
        query.addCriteria(Criteria.where("account").is(account));
        query.addCriteria(Criteria.where("sequence").is(sequence));
        return this.mongoOps.findOne(query, PersistedV2Key.class, KEY_COLLECTION);
    }

    public int getNextAvailableSequence(String walletKey, int account) {
        List<PersistedV2Key> keys = getKeys(walletKey, account);
        Collections.sort(keys, new Comparator<PersistedV2Key>() {
            @Override
            public int compare(PersistedV2Key k1, PersistedV2Key k2) {
                return (k2.sequence > k1.sequence) ? 1 : -1;
            }
        });
        if (keys.size() > 0) {
            PersistedV2Key last = keys.get(0);
            return last.sequence + 1;
        } else {
            return 0;
        }
    }

    public void drop() {
        this.mongoOps.dropCollection(KEY_COLLECTION);
    }
}
