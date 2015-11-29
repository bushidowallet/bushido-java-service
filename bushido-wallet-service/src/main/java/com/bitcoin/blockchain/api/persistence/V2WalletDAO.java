package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedV2WalletDescriptor;
import com.bitcoin.blockchain.api.domain.V2WalletAccount;

import java.util.List;

/**
 * Created by Jesion on 2015-01-20.
 */
public interface V2WalletDAO {

    public void create(PersistedV2WalletDescriptor wallet);

    public PersistedV2WalletDescriptor get(String key);

    public List<PersistedV2WalletDescriptor> getUserWallets(String userId);

    public List<PersistedV2WalletDescriptor> getAll();

    public boolean hasWallet(String key);

    public void updateAccounts(String key, List<V2WalletAccount> accounts);

    public boolean isValid(PersistedV2WalletDescriptor wallet);

    public void delete(String key);

    public void drop();
}
