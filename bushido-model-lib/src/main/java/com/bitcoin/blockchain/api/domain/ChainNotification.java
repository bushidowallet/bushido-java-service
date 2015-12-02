package com.bitcoin.blockchain.api.domain;

import java.util.ArrayList;

/**
 * Created by Jesion on 2015-06-25.
 */
public class ChainNotification {

    public String type;
    public String block_chain;
    public ChainTransaction transaction;

    public ChainNotification() {

    }
}
