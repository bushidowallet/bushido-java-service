package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-05-21.
 */
public class ExchangeInfo implements Serializable {

    public Exchange exchange;
    public ExchangeData data;

    public ExchangeInfo() {

    }

    public ExchangeInfo(Exchange exchange, ExchangeData data) {
        this.exchange = exchange;
        this.data = data;
    }
}
