package com.bitcoin.blockchain.api.domain.message;

import java.io.Serializable;

/**
 * Created by Jesion on 2014-12-04.
 *
 * This class represents a message sent from the client
 */
public class ClientMessage extends ClientMessageBase implements IMessage, Serializable {

    public ClientMessage() {
        super();
    }

    public ClientMessage(String username, String password, String key, String command, Object payload) {
        this(key, command, payload);
        setUsername(username);
        setPassword(password);
    }

    public ClientMessage(String key, String command, Object payload) {
        super();
        setCommand(command);
        setKey(key);
        this.payload = payload;
    }

    public ClientMessage(String correlationId, String key, String command) {
        super();
        setCorrelationId(correlationId);
        setCommand(command);
        setKey(key);
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return this.payload;
    }
}
