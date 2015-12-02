package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.TransactionStatus;

/**
 * Created by Jesion on 2015-02-04.
 */
public class TransactionStatusMessage extends Message implements IMessage {

    public TransactionStatusMessage() {
        super();
    }

    public TransactionStatusMessage(String command, String correlationId, TransactionStatus payload) {
        super();
        setCommand(command);
        setCorrelationId(correlationId);
        setPayload(payload);
    }

    public TransactionStatus getPayload() {
        return (TransactionStatus) this.payload;
    }

    public void setPayload(TransactionStatus payload) {
        this.payload = payload;
    }
}
