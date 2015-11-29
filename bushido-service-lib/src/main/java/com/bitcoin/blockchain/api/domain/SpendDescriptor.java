package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-03-24.
 */
public class SpendDescriptor {

    public String receivingAddress;

    public long value;

    public SpendDescriptor() {

    }

    public SpendDescriptor(String receivingAddress, long value) {
        this.receivingAddress = receivingAddress;
        this.value = value;
    }
}
