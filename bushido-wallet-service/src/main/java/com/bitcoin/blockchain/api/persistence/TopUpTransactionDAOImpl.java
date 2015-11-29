package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedTopUpTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by Jesion on 2015-10-23.
 */
@Service("topUpTransactionDAO")
public class TopUpTransactionDAOImpl implements TopUpTransactionDAO {

    public static String TOPUPTRANSACTION_COLLECTION = "topuptransaction";

    @Autowired
    public MongoOperations mongoOps;

    public void create(PersistedTopUpTransaction transaction) {
        this.mongoOps.insert(transaction, TOPUPTRANSACTION_COLLECTION);
    }

    public List<PersistedTopUpTransaction> getByWallet(String walletId) {
        Query query = new Query(Criteria.where("walletKey").is(walletId));
        return this.mongoOps.find(query, PersistedTopUpTransaction.class, TOPUPTRANSACTION_COLLECTION);
    }

    public List<PersistedTopUpTransaction> getByWalletAndCurrency(String walletId, String currency) {
        Query query = new Query(Criteria.where("walletKey").is(walletId).and("currency").is(currency));
        return this.mongoOps.find(query, PersistedTopUpTransaction.class, TOPUPTRANSACTION_COLLECTION);
    }

    public List<PersistedTopUpTransaction> getByUser(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return this.mongoOps.find(query, PersistedTopUpTransaction.class, TOPUPTRANSACTION_COLLECTION);
    }

    public PersistedTopUpTransaction getByControl(String control) {
        Query query = new Query(Criteria.where("control").is(control));
        return this.mongoOps.findOne(query, PersistedTopUpTransaction.class, TOPUPTRANSACTION_COLLECTION);
    }

    public void update(PersistedTopUpTransaction tx) {
        final Query query = new Query(Criteria.where("id").is(tx.getId()));
        final Update update = new Update();
        update.set("status", tx.status);
        update.set("amount", tx.amount);
        update.set("date", tx.date);
        update.set("gateway", tx.gateway);
        update.set("gatewayTransactionId", tx.gatewayTransactionId);
        update.set("currency", tx.currency);
        update.set("originalAmount", tx.originalAmount);
        update.set("commission", tx.commission);
        update.set("widthdrawalAmount", tx.widthdrawalAmount);
        this.mongoOps.upsert(query, update, PersistedTopUpTransaction.class, TOPUPTRANSACTION_COLLECTION);
    }
}
