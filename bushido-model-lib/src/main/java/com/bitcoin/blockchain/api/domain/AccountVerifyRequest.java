package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-11-17.
 */
public class AccountVerifyRequest extends TokenRequest {

    public String email;

    public AccountVerifyRequest() {
        super();
    }

    public AccountVerifyRequest(String token, String email) {
        super(token);
        this.email = email;
    }
}
