package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.TransactionOutpoint;

import java.util.List;

/**
 * Created by Jesion on 2015-02-17.
 */
public interface TransactionOutpointDAO {

    public void save(List<TransactionOutpoint> outpoints);

    public List<TransactionOutpoint> list(String walletKey);

    public void drop();
}
