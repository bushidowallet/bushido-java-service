package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-11-13.
 */
public class PasswordResetConfirmationRequest extends TokenRequest {

    public String password;

    public PasswordResetConfirmationRequest() {

    }

    public PasswordResetConfirmationRequest(String token, String password) {
        super(token);
        this.password = password;
    }
}
