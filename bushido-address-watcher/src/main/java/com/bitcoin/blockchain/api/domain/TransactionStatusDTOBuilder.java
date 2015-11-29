package com.bitcoin.blockchain.api.domain;

import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.TransactionConfidence;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Jesion on 2015-02-25.
 */
public class TransactionStatusDTOBuilder {

    private TransactionConfidence confidence;
    private String txHash;

    public TransactionStatusDTOBuilder(String txHash, TransactionConfidence confidence) {
        this.txHash = txHash;
        this.confidence = confidence;
    }

    public TransactionStatus build() {
        TransactionStatus status =  new TransactionStatus();
        status.txHash = txHash;
        ListIterator<PeerAddress> i = confidence.getBroadcastBy();
        List<String> b = new ArrayList<String>();
        while (i.hasNext()) {
            PeerAddress a = i.next();
            b.add(a.getAddr().getHostAddress());
        }
        status.broadcasters = b;
        status.confirmations = confidence.getDepthInBlocks();
        String s = "";
        final TransactionConfidence.ConfidenceType c = confidence.getConfidenceType();
        if (c.equals(TransactionConfidence.ConfidenceType.BUILDING)) {
            s = TransactionStatus.CONFIRMED;
        } else if ( c.equals(TransactionConfidence.ConfidenceType.PENDING) ) {
            s = TransactionStatus.PENDING;
        } else if ( c.equals(TransactionConfidence.ConfidenceType.DEAD)) {
            s = "Dead";
        } else if ( c.equals(TransactionConfidence.ConfidenceType.UNKNOWN)) {
            s = "Unknown";
        }
        status.status = s;
        return status;
    }
}