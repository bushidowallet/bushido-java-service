package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.WalletInfo;

/**
 * Created by Jesion on 2015-07-16.
 */
public class GetAddressMessage extends Message implements IMessage {

    public GetAddressMessage() {
        super();
    }

    public GetAddressMessage(String command, String correlationId, WalletInfo payload, String key) {
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

    public WalletInfo getPayload() {
        return (WalletInfo) this.payload;
    }

    public void setPayload(WalletInfo payload) {
        this.payload = payload;
    }
}
