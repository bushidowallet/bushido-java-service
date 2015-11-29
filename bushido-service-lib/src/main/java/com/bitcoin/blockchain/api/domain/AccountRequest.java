package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-11-12.
 */
public class AccountRequest implements Serializable {

    public String email;

    public AccountRequest()  {

    }

    public AccountRequest(String email) {
        this.email = email;
    }
}
