package com.bitcoin.blockchain.api.domain.message;


import com.bitcoin.blockchain.api.domain.V2Key;

/**
 * Created by Jesion on 2015-02-04.
 */
public class KeyMessage extends Message implements IMessage {

    public KeyMessage() {
        super();
    }

    public KeyMessage(String command, String correlationId, V2Key payload) {
        super();
        setCommand(command);
        setCorrelationId(correlationId);
        setPayload(payload);
    }

    public V2Key getPayload() {
        return (V2Key) this.payload;
    }

    public void setPayload(V2Key payload) {
        this.payload = payload;
    }
}
