package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedTopUpTransaction;

import java.util.List;

/**
 * Created by Jesion on 2015-10-23.
 */
public interface TopUpTransactionDAO {

    public void create(PersistedTopUpTransaction transaction);

    public List<PersistedTopUpTransaction> getByWallet(String walletId);

    public List<PersistedTopUpTransaction> getByWalletAndCurrency(String walletId, String currency);

    public List<PersistedTopUpTransaction> getByUser(String userId);

    public PersistedTopUpTransaction getByControl(String control);

    public void update(PersistedTopUpTransaction tx);
}
