package com.bitcoin.blockchain.api.domain.message;

/**
 * Created by Jesion on 2015-02-04.
 */
public interface IMessage {

    public String getCommand();

    public String getCorrelationId();

    public Object getPayload();
}
