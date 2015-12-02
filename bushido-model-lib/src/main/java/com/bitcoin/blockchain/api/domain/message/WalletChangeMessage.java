package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.WalletChange;

/**
 * Created by Jesion on 2015-02-26.
 */
public class WalletChangeMessage extends Message implements IMessage {

    public WalletChangeMessage() {
        super();
    }

    public WalletChangeMessage(String command, String correlationId, WalletChange payload, String key) {
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

    public WalletChange getPayload() {
        return (WalletChange) this.payload;
    }

    public void setPayload(WalletChange payload) {
        this.payload = payload;
    }
}
