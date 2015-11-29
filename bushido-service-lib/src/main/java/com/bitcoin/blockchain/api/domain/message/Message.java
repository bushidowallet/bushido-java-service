package com.bitcoin.blockchain.api.domain.message;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-02-04.
 */
public abstract class Message implements Serializable {

    private String command;

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private String correlationId;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId)
    {
        this.correlationId = correlationId;
    }

    protected Object payload;
}
