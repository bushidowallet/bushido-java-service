package com.bitcoin.blockchain.api.util;

import com.bitcoin.blockchain.api.domain.TransactionInput;
import com.bitcoin.blockchain.api.domain.TransactionOutpoint;

/**
 * Created by Jesion on 2015-02-26.
 */
public class TransactionUtil {

    public static TransactionOutpoint getOutpoint(TransactionInput input) {
        return new TransactionOutpoint(input.getOutpointTransactionHash(), input.getOutpointTransactionOutputIndex());
    }
}
