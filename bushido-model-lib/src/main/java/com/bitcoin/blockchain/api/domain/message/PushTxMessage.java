package com.bitcoin.blockchain.api.domain.message;

/**
 * Created by Jesion on 2015-03-19.
 */
public class PushTxMessage extends Message implements IMessage {

    public PushTxMessage() {
        super();
    }

    public PushTxMessage(String command, String correlationId, String payload) {
        super();
        setCommand(command);
        setCorrelationId(correlationId);
        setPayload(payload);
    }

    public String getPayload() {
        return (String) this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
