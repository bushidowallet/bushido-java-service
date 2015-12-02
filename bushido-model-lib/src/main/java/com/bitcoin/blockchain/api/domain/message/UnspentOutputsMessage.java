package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.TransactionOutputList;

import java.util.Map;

/**
 * Created by Jesion on 2015-03-13.
 */
public class UnspentOutputsMessage extends Message implements IMessage {

    public UnspentOutputsMessage() {
        super();
    }

    public UnspentOutputsMessage(String command, String correlationId, Map<String, TransactionOutputList> payload, String key) {
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

    public Map<String, TransactionOutputList> getPayload() {
        return (Map<String, TransactionOutputList>) this.payload;
    }

    public void setPayload(Map<String, TransactionOutputList> payload) {
        this.payload = payload;
    }
}

