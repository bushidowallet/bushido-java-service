package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.BalanceChange;

/**
 * Created by Jesion on 2015-03-03.
 */
public class BalanceChangeMessage extends Message implements IMessage {

    public BalanceChangeMessage() {
        super();
    }

    public BalanceChangeMessage(String command, String correlationId, BalanceChange payload, String key) {
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

    public BalanceChange getPayload() {
        return (BalanceChange) this.payload;
    }

    public void setPayload(BalanceChange payload) {
        this.payload = payload;
    }
}
