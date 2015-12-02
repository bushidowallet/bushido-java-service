package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-07-15.
 */
public class Token2FARequest implements Serializable {

    public String username;
    public boolean enforceSms;

    public Token2FARequest() {

    }

    public Token2FARequest(String username, boolean enforceSms) {
        this.username = username;
        this.enforceSms = enforceSms;
    }
}
