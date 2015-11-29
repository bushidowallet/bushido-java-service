package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedV2WalletDescriptor;
import com.bitcoin.blockchain.api.domain.V2WalletAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jesion on 2014-12-25.
 */
@Service("v2walletDAO")
public class V2WalletDAOImpl implements V2WalletDAO {

    public static String WALLET_COLLECTION = "v2wallet";

    @Autowired
    public MongoOperations mongoOps;

    public void create(PersistedV2WalletDescriptor wallet) {
        this.mongoOps.insert(wallet, WALLET_COLLECTION);
    }

    public void updateAccounts(String key, List<V2WalletAccount> accounts) {
        final Query query = new Query(Criteria.where("key").is(key));
        final Update update = new Update();
        update.set("accounts", accounts);
        this.mongoOps.updateFirst(query, update, PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
    }

    public PersistedV2WalletDescriptor get(String key) {
        Query query = new Query(Criteria.where("key").is(key));
        return this.mongoOps.findOne(query, PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
    }

    public void delete(String key) {
        Query query = new Query(Criteria.where("key").is(key));
        this.mongoOps.remove(query, PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
    }

    public List<PersistedV2WalletDescriptor> getAll() {
        return this.mongoOps.findAll(PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
    }

    public boolean hasWallet(String key) {
        Query query = new Query(Criteria.where("key").is(key));
        PersistedV2WalletDescriptor wallet = this.mongoOps.findOne(query, PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
        if (wallet != null) {
            return true;
        }
        return false;
    }

    public boolean isValid(PersistedV2WalletDescriptor wallet) {
        Query query = new Query(Criteria.where("key").is(wallet.key).and("owner").is(wallet.owner));
        PersistedV2WalletDescriptor persisted = this.mongoOps.findOne(query, PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
        if (persisted != null) {
            return true;
        }
        return false;
    }

    public List<PersistedV2WalletDescriptor> getUserWallets(String userId) {
        Query query = new Query(Criteria.where("owner").is(userId));
        return this.mongoOps.find(query, PersistedV2WalletDescriptor.class, WALLET_COLLECTION);
    }

    public void drop() {
        this.mongoOps.dropCollection(WALLET_COLLECTION);
    }
}
