package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.InstrumentChange;

/**
 * Created by Jesion on 2015-05-22.
 */
public class InstrumentChangeMessage extends Message implements IMessage {

    public InstrumentChangeMessage() {
        super();
    }

    public InstrumentChangeMessage(String command, String correlationId, InstrumentChange payload, String key) {
        super();
        setCommand(command);
        setCorrelationId(correlationId);
        setPayload(payload);
        setKey(key);
    }

    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public InstrumentChange getPayload() {
        return (InstrumentChange) this.payload;
    }

    public void setPayload(InstrumentChange payload) {
        this.payload = payload;
    }
}
