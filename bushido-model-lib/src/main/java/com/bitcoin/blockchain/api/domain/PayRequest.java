package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-03-13.
 */
public class PayRequest implements Serializable {

    //contains signed inputs list
    //contains outputs list (atm, single output, pay to pub key hash type transaction (value, address))
    //contains long fee
    private Transaction tx;

    //this is filled out when arriving in backend-wallet (if keys served by the backend)
    //otherwise, when using a standalone wallet which is maintaining its HD key tree, it has to be provided
    private String changeAddress;

    public PayRequest() {

    }

    public PayRequest(Transaction tx, String changeAddress) {
        this.tx = tx;
        this.changeAddress = changeAddress;
    }
}
