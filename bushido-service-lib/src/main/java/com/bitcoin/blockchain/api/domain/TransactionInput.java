package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2014-12-02.
 */
public class TransactionInput implements Serializable {

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    private long sequence;

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    private String fromAddress;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    private String script;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    //HEX string representing script bytes
    private String scriptBytes;

    public String getScriptBytes() {
        return scriptBytes;
    }

    public void setScriptBytes(String scriptPubKey) {
        this.scriptBytes = scriptPubKey;
    }

    private String outpointTransactionHash;

    public String getOutpointTransactionHash() {
        return this.outpointTransactionHash;
    }

    public void setOutpointTransactionHash(String outpointTransactionHash) {
        this.outpointTransactionHash = outpointTransactionHash;
    }

    private long outpointTransactionOutputIndex;

    public long getOutpointTransactionOutputIndex() {
        return this.outpointTransactionOutputIndex;
    }

    public void setOutpointTransactionOutputIndex(long outpointTransactionOutputIndex) {
        this.outpointTransactionOutputIndex = outpointTransactionOutputIndex;
    }

    public TransactionInput() {

    }
}
