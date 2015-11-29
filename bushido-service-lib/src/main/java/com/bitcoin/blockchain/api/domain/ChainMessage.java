package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-06-25.
 */
public class ChainMessage {

    public String id;
    public int sequence;
    public String created_at;
    public int delivery_attempt;
    public String notification_id;
    public ChainNotification payload;

    public ChainMessage() {

    }
}
