package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2014-12-02.
 */
public class Balance implements Serializable {

    public long confirmed;

    public long unconfirmed;

    public Balance(long confirmed, long unconfirmed) {
        this.confirmed = confirmed;
        this.unconfirmed = unconfirmed;
    }

    public Balance() {

    }

    public Balance clone() {
        return new Balance(this.confirmed, this.unconfirmed);
    }
}
