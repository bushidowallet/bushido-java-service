package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2015-06-26.
 */
public class UserLoginRequest implements Serializable {

    public String userIdOrEmail;

    public List<String> credentials;

    public UserLoginRequest()  {

    }

    public UserLoginRequest(String userIdOrEmail, List<String> credentials) {
        this.userIdOrEmail = userIdOrEmail;
        this.credentials = credentials;
    }
}
