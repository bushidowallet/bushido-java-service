package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedTransaction;
import com.bitcoin.blockchain.api.domain.TransactionStatus;

import java.util.List;

/**
 * Created by Jesion on 2014-12-10.
 */
public interface TransactionDAO {

    public void create(PersistedTransaction tx);

    public void create(List<PersistedTransaction> tx);

    public void update(PersistedTransaction tx, TransactionStatus status);

    public void setBlockHash(String txHash, String blockHash);

    public List<PersistedTransaction> getTransactions(String walletKey, int account);

    public List<PersistedTransaction> getTransactions(String walletKey);

    public List<PersistedTransaction> getSpendingTransactions(String walletKey);

    public List<PersistedTransaction> getBlockTransactions(String blockHash);

    public PersistedTransaction readByHash(String hash);

    public List<PersistedTransaction> readByHash(List<String> hash);

    public void drop();
}
