package com.bitcoin.blockchain.api.domain;

import com.bushidowallet.core.crypto.util.ByteUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Jesion on 2015-02-26.
 */
@Document
public class PersistedTransaction extends Transaction {

    @Id
    private String id;

    public PersistedTransaction() {
        super();
    }

    public PersistedTransaction(String hash,
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
        super(hash,
            totalInput,
            totalOutput,
            inputs,
            outputs,
            confirmations,
            status,
            fee,
            memo,
            size,
            ver,
            blockHash,
            time,
            value
        );
    }

    public PersistedTransaction(Transaction t) {
        this(t.hash,
            t.totalInput,
            t.totalOutput,
            t.inputs,
            t.outputs,
            t.confirmations,
            t.status,
            t.fee,
            t.memo,
            t.size,
            t.ver,
            t.blockHash,
            t.time,
            t.value
        );
    }

    public String toString() {
        String txStr = "TX hash: " + this.hash + " : inputs : ";
        for (int i = 0; i < this.inputs.size(); i++) {
            TransactionInput input = this.inputs.get(i);
            txStr = txStr + " script str: " + input.getScript();
            txStr = txStr + " scriptBytes: " + input.getScriptBytes();
            byte[] inputScript = ByteUtil.fromHex(input.getScriptBytes());
            txStr = txStr + " byte[] len: " + inputScript.length;
        }
        txStr = txStr + " : outputs : ";
        for (int j = 0; j < this.outputs.size(); j++) {
            TransactionOutput output = this.outputs.get(j);
            txStr = txStr + " script str: " + output.getScript();
            txStr = txStr + " scriptBytes: " + output.getScriptBytes();
            byte[] outputScript = ByteUtil.fromHex(output.getScriptBytes());
            txStr = txStr + " byte[] len: " + outputScript.length;
        }
        return txStr;
    }
}
