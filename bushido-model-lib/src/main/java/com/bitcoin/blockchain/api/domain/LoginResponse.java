package com.bitcoin.blockchain.api.domain;

import java.util.List;

/**
 * Created by Jesion on 2015-02-25.
 */
public class LoginResponse extends ResponseBase {

    public LoginResponse() {
        super();
    }

    public LoginResponse(List<Error> errors) {
        super(errors);
    }

    public LoginResponse(List<Error> errors, V2WalletDescriptor payload) {
        super(errors);
        setPayload(payload);
    }

    public LoginResponse(V2WalletDescriptor payload) {
        super();
        setPayload(payload);
    }

    private V2WalletDescriptor payload;

    public void setPayload(V2WalletDescriptor payload) {
        this.payload = payload;
    }

    public V2WalletDescriptor getPayload() {
        return this.payload;
    }
}
