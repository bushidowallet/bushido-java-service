package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2014-12-02.
 */
public class TransactionOutput implements Serializable {

    private V2Key key;

    public V2Key getKey() {
        return key;
    }

    public void setKey(V2Key key) {
        this.key = key;
    }

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    private String toAddress;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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

    private String txHash;

    /**
     * Transaction hash, that was sending this output to our address
     *
     * @return
     */
    public String getTxHash() {
        return this.txHash;
    }

    public void setTxHash(String value) {
        this.txHash = value;
    }

    private int index;

    /**
     * Index of this output in a transaction outputs list
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TransactionOutput() {

    }

    public String toString() {
        return "Transaction output value: " + getValue() + " script: " + getScript() + " to addr: " + getToAddress() + " tx hash: " + txHash + " index: " + index;
    }
}
