package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-12-17.
 */
public class UserPin implements Serializable {

    public String pin;
    public String username;

    public UserPin(String username, String pin) {

        this.username = username;
        this.pin = pin;
    }

    public UserPin() {

    }
}
