package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedV2Key;

import java.util.List;

/**
 * Created by Jesion on 2015-01-21.
 */
public interface V2WalletKeyDAO {

    public void logKey(PersistedV2Key key);

    public List<PersistedV2Key> find(String address);

    public PersistedV2Key get(String id);

    public List<PersistedV2Key> getKeys(String walletKey, int account);

    public List<PersistedV2Key> getKeys(List<String> ids);

    public PersistedV2Key getKey(String walletKey, int account, int sequence);

    public int getNextAvailableSequence(String walletKey, int account);

    public void drop();
}
