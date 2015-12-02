package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.SpendDescriptor;

import java.util.List;

/**
 * Created by Jesion on 2015-03-24.
 */
public class SpendMessage extends ClientMessageBase implements IMessage {

    public SpendMessage() {
        super();
    }

    public SpendMessage(String username, String password, String key, String command, List<SpendDescriptor> payload) {
        super();
        setUsername(username);
        setPassword(password);
        setKey(key);
        setCommand(command);
        setPayload(payload);
    }

    public List<SpendDescriptor> getPayload() {
        return (List<SpendDescriptor>) this.payload;
    }

    public void setPayload(List<SpendDescriptor> payload) {
        this.payload = payload;
    }
}
