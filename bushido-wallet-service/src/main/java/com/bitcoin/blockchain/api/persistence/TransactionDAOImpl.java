package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedTransaction;
import com.bitcoin.blockchain.api.domain.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jesion on 2014-12-10.
 */
@Service("transactionDAO")
public class TransactionDAOImpl implements TransactionDAO {

    public static String TRANSACTION_COLLECTION = "transaction";

    @Autowired
    public MongoOperations mongoOps;

    public void create(PersistedTransaction tx) {
        this.mongoOps.insert(tx, TRANSACTION_COLLECTION);
    }

    public void create(List<PersistedTransaction> tx) {
        this.mongoOps.insert(tx, TRANSACTION_COLLECTION);
    }

    public PersistedTransaction readByHash(String hash) {
        final Query query = new Query(Criteria.where("hash").is(hash));
        return this.mongoOps.findOne(query, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public List<PersistedTransaction> readByHash(List<String> hash) {
        final Query query = new Query(Criteria.where("hash").in(hash));
        return mongoOps.find(query, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public void setBlockHash(String txHash, String blockHash) {
        Query query = new Query(Criteria.where("hash").is(txHash));
        Update update = new Update().set("blockHash", blockHash);
        this.mongoOps.findAndModify(query, update, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public void update(PersistedTransaction tx, TransactionStatus status) {
        Query query = new Query(Criteria.where("hash").is(tx.hash));
        Update update = new Update();
        update.set("blockHash", status.blockHash);
        update.set("confirmations", status.confirmations);
        update.set("status", status.status);
        this.mongoOps.findAndModify(query, update, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public List<PersistedTransaction> getBlockTransactions(String blockHash) {
        Query query = new Query(Criteria.where("blockHash").is(blockHash));
        return this.mongoOps.find(query, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public List<PersistedTransaction> getTransactions(String walletKey, int account) {
        Query query = new Query(Criteria.where("walletId").is(walletKey).and("account").is(account));
        return this.mongoOps.find(query, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public List<PersistedTransaction> getTransactions(String walletKey) {
        Query query = new Query(Criteria.where("walletId").is(walletKey));
        return this.mongoOps.find(query, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public List<PersistedTransaction> getSpendingTransactions(String walletKey) {
        Query query = new Query(Criteria.where("walletId").is(walletKey));
        return this.mongoOps.find(query, PersistedTransaction.class, TRANSACTION_COLLECTION);
    }

    public void drop() {
        this.mongoOps.dropCollection(TRANSACTION_COLLECTION);
    }
}
