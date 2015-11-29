package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.TransactionOutpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jesion on 2015-02-17.
 */
@Service("transactionOutpointDAO")
public class TransactionOutpointDAOImpl implements TransactionOutpointDAO {

    private static String OUTPOINT_COLLECTION = "outpoint";

    @Autowired
    public MongoOperations mongoOps;

    public void save(List<TransactionOutpoint> outpoints) {

        this.mongoOps.insert(outpoints, OUTPOINT_COLLECTION);
    }

    public List<TransactionOutpoint> list(String walletKey) {
        Query query = new Query(Criteria.where("walletKey").is(walletKey));
        return this.mongoOps.find(query, TransactionOutpoint.class, OUTPOINT_COLLECTION);
    }

    public void drop() {
        this.mongoOps.dropCollection(OUTPOINT_COLLECTION);
    }
}
