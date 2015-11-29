package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-11-14.
 */
public class TokenRequest implements Serializable {

    public String token;

    public TokenRequest() {

    }

    public TokenRequest(String token) {
        this.token = token;
    }
}
