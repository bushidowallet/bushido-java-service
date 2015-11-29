package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2014-11-30.
 */
public class Transaction implements Serializable {

    public String keyId;

    public String walletId;

    public int account;

    public String hash;

    public long totalInput;

    public long totalOutput;

    public List<TransactionInput> inputs;

    public List<TransactionOutput> outputs;

    public int confirmations;

    public String status;

    public long fee;

    public String memo;

    public int size;

    public long ver;

    public String blockHash;

    public long time;

    public long value;

    public Transaction() {

    }

    public Transaction(String hash,
                       long totalInput,
                       long totalOutput,
                       List<TransactionInput> inputs,
                       List<TransactionOutput> outputs,
                       int confirmations,
                       String status,
                       long fee,
                       String memo,
                       int size,
                       long ver,
                       String blockHash,
                       long time,
                       long value) {
        this.hash = hash;
        this.totalInput = totalInput;
        this.totalOutput = totalOutput;
        this.inputs = inputs;
        this.outputs = outputs;
        this.confirmations = confirmations;
        this.status = status;
        this.fee = fee;
        this.memo = memo;
        this.size = size;
        this.ver = ver;
        this.blockHash = blockHash;
        this.time = time;
        this.value = value;
    }

    public TransactionOutput getOutput(int index) {
        return this.outputs.get(index);
    }
}
