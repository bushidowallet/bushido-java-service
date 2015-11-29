package com.bitcoin.blockchain.api.domain;

import com.bitcoin.blockchain.api.util.TransactionUtil;
import com.bushidowallet.core.crypto.util.ByteUtil;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.Utils;
import org.bitcoinj.script.Script;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-02-25.
 */
public class PersistedTransactionDTOBuilder {

    private org.bitcoinj.core.Transaction tx;

    private TransactionUtil util;

    private NetworkParameters parameters;

    private String blockHash;

    private org.bitcoinj.core.Wallet wallet;

    public PersistedTransactionDTOBuilder(org.bitcoinj.core.Transaction tx,
                                          TransactionUtil util,
                                          NetworkParameters parameters,
                                          String blockHash) {
        this(tx, util, parameters);
        this.blockHash = blockHash;
    }

    public PersistedTransactionDTOBuilder(org.bitcoinj.core.Transaction tx,
                                          TransactionUtil util,
                                          NetworkParameters parameters) {
        this.tx = tx;
        this.util = util;
        this.parameters = parameters;
    }

    public PersistedTransactionDTOBuilder(org.bitcoinj.core.Transaction tx,
                                          TransactionUtil util,
                                          NetworkParameters parameters,
                                          org.bitcoinj.core.Wallet wallet) {
        this(tx, util, parameters);
        this.wallet = wallet;
    }

    private String getToAddress(Script script, NetworkParameters parameters) {
        if (script.isSentToAddress() || script.isPayToScriptHash())
            return script.getToAddress(parameters).toString();
        else if (script.isSentToRawPubKey())
            return Utils.HEX.encode(script.getPubKey());
        else if (script.isSentToMultiSig())
            return "multisig";
        else
            return "unknown";
    }

    public Transaction build() {
        final List<TransactionInput> inputs = new ArrayList<TransactionInput>();
        final List<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
        for (org.bitcoinj.core.TransactionInput txInput : tx.getInputs()) {
            TransactionInput input = new TransactionInput();
            if (txInput.getValue() != null) {
                input.setValue(txInput.getValue().getValue());
            }
            try {
                input.setScript(txInput.getScriptSig().toString());
                String scriptBytes = ByteUtil.toHex(txInput.getScriptBytes());
                input.setScriptBytes(scriptBytes);
                System.out.println("Constructing TI with script bytes: " + scriptBytes + " byte[] len " + txInput.getScriptBytes().length);
                System.out.println("Corresponding readable input script string: " + input.getScript());
                input.setSequence(txInput.getSequenceNumber());
                //refers to output of another transaction... can load that and read value from there
                //but only if this not a CoinBase
                if (txInput.isCoinBase() == false) {
                    input.setFromAddress(txInput.getFromAddress().toString());
                    input.setOutpointTransactionHash(txInput.getOutpoint().getHash().toString());
                    input.setOutpointTransactionOutputIndex(txInput.getOutpoint().getIndex());
                } else {
                    //each block has its coinbase - so commenting out for now as its flooding the output console...
                    //System.out.println("Found coinbase: " + tx.getHashAsString());
                }
            } catch (Exception e) {

            }
            inputs.add(input);
        }
        for (org.bitcoinj.core.TransactionOutput txOutput : tx.getOutputs()) {
            TransactionOutput output = new TransactionOutput();
            output.setValue(txOutput.getValue().getValue());
            try {
                output.setScript(txOutput.getScriptPubKey().toString());
                String scriptBytes = ByteUtil.toHex(txOutput.getScriptBytes());
                System.out.println("Constructing TO with scriptBytes bytes: " + scriptBytes + " byte[] len " + txOutput.getScriptBytes().length);
                System.out.println("Corresponding readable output script string: " + output.getScript());
                output.setScriptBytes(scriptBytes);
                output.setToAddress(getToAddress(txOutput.getScriptPubKey(), parameters));
                output.setTxHash(txOutput.getParentTransaction().getHashAsString());
                output.setIndex(txOutput.getIndex());
            } catch (Exception e) {

            }
            outputs.add(output);
        }
        long fee = -1;
        if (tx.getFee() != null) {
            fee = tx.getFee().getValue();
        }
        String s = "";
        final TransactionConfidence.ConfidenceType c = tx.getConfidence().getConfidenceType();
        if (c.equals(TransactionConfidence.ConfidenceType.BUILDING)) {
            s = TransactionStatus.CONFIRMED;
        } else if ( c.equals(TransactionConfidence.ConfidenceType.PENDING) ) {
            s = TransactionStatus.PENDING;
        } else if ( c.equals(TransactionConfidence.ConfidenceType.DEAD)) {
            s = "Dead";
        } else if ( c.equals(TransactionConfidence.ConfidenceType.UNKNOWN)) {
            s = "Unknown";
        }
        return new PersistedTransaction(tx.getHashAsString(),
                0,
                util.calculateTotalOutputs(tx),
                inputs,
                outputs,
                tx.getConfidence().getDepthInBlocks(),
                s,
                fee,
                tx.getMemo(),
                tx.getMessageSize(),
                tx.getVersion(),
                blockHash,
                tx.getUpdateTime().getTime(),
                (wallet != null) ? tx.getValue(this.wallet).getValue() : 0);
    }
}