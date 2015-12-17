package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-12-17.
 */
public class RegUserPin extends UserPin {

    public String regCode;

    public RegUserPin(String username, Number pin) {
        super(username, pin);
    }

    public RegUserPin() {

    }
}
