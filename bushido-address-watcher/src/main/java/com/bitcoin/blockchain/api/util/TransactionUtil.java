package com.bitcoin.blockchain.api.util;

import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;

import java.util.List;

/**
 * Created by Jesion on 2014-12-02.
 */
public class TransactionUtil {

    public long calculateTotalOutputs(Transaction tx) {
        long total = 0;
        final List<TransactionOutput> outputs = tx.getOutputs();
        for (TransactionOutput output : outputs) {
            total += output.getValue().getValue();
        }
        return total;
    }
}
