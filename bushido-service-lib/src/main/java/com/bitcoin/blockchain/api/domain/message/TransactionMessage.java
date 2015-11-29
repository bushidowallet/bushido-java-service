package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.Transaction;

/**
 * Created by Jesion on 2015-02-04.
 */
public class TransactionMessage extends Message implements IMessage {

    public TransactionMessage() {
        super();
    }

    public TransactionMessage(String command, String correlationId, Transaction payload) {
        super();
        setCommand(command);
        setCorrelationId(correlationId);
        setPayload(payload);
    }

    public Transaction getPayload() {
        return (Transaction) this.payload;
    }

    public void setPayload(Transaction payload) {
        this.payload = payload;
    }
}
