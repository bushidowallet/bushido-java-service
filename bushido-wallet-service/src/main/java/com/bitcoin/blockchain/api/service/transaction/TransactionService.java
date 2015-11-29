package com.bitcoin.blockchain.api.service.transaction;

import com.bitcoin.blockchain.api.domain.ChainMessage;
import com.bitcoin.blockchain.api.domain.Response;

/**
 * Created by Jesion on 2014-12-29.
 */
public interface TransactionService {

    public Response push(String tx);

    public Response notifyConfirmed(ChainMessage tx);
}
