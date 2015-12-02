package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-06-26.
 */
public class UserLoginRequest implements Serializable {

    public String userIdOrEmail;

    public String credential;

    public UserLoginRequest()  {

    }

    public UserLoginRequest(String userIdOrEmail, String credential) {
        this.userIdOrEmail = userIdOrEmail;
        this.credential = credential;
    }
}
