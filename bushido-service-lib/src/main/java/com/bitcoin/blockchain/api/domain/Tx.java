package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-03-19.
 */
public class Tx implements Serializable {

    public String hex;

    public Tx(String hex) {
        this.hex = hex;
    }

    public Tx() {

    }
}
