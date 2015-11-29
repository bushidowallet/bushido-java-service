package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-05-21.
 */
public class Instrument implements Serializable {

    public String id;

    public String name;

    public Instrument() {

    }

    public Instrument(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
