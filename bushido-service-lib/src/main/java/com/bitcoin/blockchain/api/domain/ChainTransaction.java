package com.bitcoin.blockchain.api.domain;

import java.util.List;

/**
 * Created by Jesion on 2015-06-26.
 */
public class ChainTransaction {

    public String hash;
    public String block_hash;
    public long block_height;
    public String block_time;
    public String chain_received_at;
    public int confirmations;
    public long lock_time;
    public List<Object> inputs;
    public List<Object> outputs;
    public long fees;
    public long amount;

    public ChainTransaction() {

    }
}
