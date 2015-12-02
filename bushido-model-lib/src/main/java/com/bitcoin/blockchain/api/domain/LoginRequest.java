package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-03-24.
 */
public class LoginRequest {

    public V2WalletDescriptor descriptor;

    public String password;

    public LoginRequest() {

    }

    public LoginRequest(V2WalletDescriptor descriptor, String password) {
        this.descriptor = descriptor;
        this.password = password;
    }
}
