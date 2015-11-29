package com.bitcoin.blockchain.api.service.transaction;

import com.bitcoin.blockchain.api.Command;
import com.bitcoin.blockchain.api.IndexerCommand;
import com.bitcoin.blockchain.api.domain.ChainMessage;
import com.bitcoin.blockchain.api.domain.Response;
import com.bitcoin.blockchain.api.domain.TransactionStatus;
import com.bitcoin.blockchain.api.domain.message.IMessage;
import com.bitcoin.blockchain.api.domain.message.PushTxMessage;
import com.bitcoin.blockchain.api.domain.message.TransactionStatusMessage;
import com.bitcoin.blockchain.api.messaging.IndexerToWalletReceiver;
import com.bitcoin.blockchain.api.messaging.WalletToIndexerSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jesion on 2014-12-29.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    public WalletToIndexerSender toIndexerSender;

    @Autowired
    public IndexerToWalletReceiver toWalletReceiver;

    public Response push(String tx) {
        Response r = new Response();
        PushTxMessage message = new PushTxMessage(IndexerCommand.BROADCAST_TX, "", tx);
        toIndexerSender.send(message);
        r.setPayload(true);
        return r;
    }

    public Response notifyConfirmed(ChainMessage tx) {
        Response r = new Response();
        TransactionStatus s = new TransactionStatus();
        s.status = tx.payload.transaction.confirmations == 0 ? TransactionStatus.PENDING : TransactionStatus.CONFIRMED;
        s.txHash = tx.payload.transaction.hash;
        s.blockHash = tx.payload.transaction.block_hash;
        s.confirmations = tx.payload.transaction.confirmations;
        IMessage message = new TransactionStatusMessage(Command.TRANSACTION_STATUS_CHANGE, null, s);
        toWalletReceiver.receive(message);
        return r;
    }
}
