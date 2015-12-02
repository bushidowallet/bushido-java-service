package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-11-15.
 */
public class SecondFactorTokenRequest extends TokenRequest {

    public String code;

    public SecondFactorTokenRequest() {

    }

    public SecondFactorTokenRequest(String code, String token) {
        super(token);
        this.code = code;
    }
}
