package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-12-17.
 */
public class UserPin implements Serializable {

    public Number pin;
    public String username;

    public UserPin(String username, Number pin) {

        this.username = username;
        this.pin = pin;
    }

    public UserPin() {

    }
}
