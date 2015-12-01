package com.bitcoin.blockchain.api.builder;

import com.bccapi.bitlib.model.*;
import com.bushidowallet.core.crypto.util.ByteUtil;

/**
 * Created by Jesion on 2015-03-16.
 */
public class UTXOBuilder {

    private NetworkParameters networkParameters;

    public UTXOBuilder(NetworkParameters networkParameters) {
        this.networkParameters = networkParameters;
    }

    public UTXOBuilder() {

    }

    /**
     * Builds an UnspentTransactionOutput object (UTXO) out of reqeuired parameters
     *
     * @param txHash - Transaction hash containing unspent output
     * @param height - height in block chain of transaction containing unspent output
     * @param scriptBytesHex - hexadecimal representation of a output script bytes
     * @param index - index of an output in containing transaction's outputs list
     * @param value - spendable value
     * @throws java.lang.Exception when there is script parsing error
     * @return unspent tx out
     */
    public UnspentTransactionOutput build(String txHash, int height, String scriptBytesHex, int index, long value) throws Exception {
        byte[] utxoScriptBytes =  ByteUtil.fromHex(scriptBytesHex);
        ScriptOutput script = ScriptOutput.fromScriptBytes(utxoScriptBytes);
        OutPoint outPoint = OutPoint.fromString(txHash + ":" + index);
        return new UnspentTransactionOutput(outPoint, height, value, script);
    }
}
