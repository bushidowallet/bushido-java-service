package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-02-19.
 */
public class TransactionOutputList implements Serializable {

    public String status;

    public List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    public long total = 0;

    public TransactionOutputList() {

    }

    public TransactionOutputList(String status) {
        this.status = status;
    }
}
