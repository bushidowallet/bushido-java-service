package com.bitcoin.blockchain.api.builder;

import com.bccapi.bitlib.model.*;
import com.bushidowallet.core.crypto.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jesion on 2015-03-16.
 */
public class UTXOTest {

    /**
     * This test only demonstrates creation of UTXO from persisted strings of real unspent output
     * fetched via RESTful API: http://localhost:8080/walletapi/client/apidoc/rest/#!/wallet/getUnspentOutputs
     */

    @Test
    public void testUTXO() {
        //Unspent Transaction Output
        //value: 10000
        //to address: 1AKR2j9nM6X3Q61Amp3vifXywthGdQoooq
        //tx hash: 8c322b170894442f94b81b49b2fce4e9855e98522bf974a7a98b5719bc608e45
        //index (output index in tx): 0
        //Script string: DUP HASH160 PUSHDATA(20)[6634adac3b80c8095797dbf90508c479b5fcedf0] EQUALVERIFY CHECKSIG
        //Script Hex bytes: 76a9146634adac3b80c8095797dbf90508c479b5fcedf088ac
        String receivingAddress = "1AKR2j9nM6X3Q61Amp3vifXywthGdQoooq";
        String utxoScriptBytesHex = "76a9146634adac3b80c8095797dbf90508c479b5fcedf088ac";
        byte[] utxoScriptBytes =  ByteUtil.fromHex(utxoScriptBytesHex);
        ScriptOutput o = null;
        Address a = null;
        try {
            o = ScriptOutput.fromScriptBytes(utxoScriptBytes);
            a = o.getAddress(NetworkParameters.productionNetwork);
        } catch (Script.ScriptParsingException e) {

        }
        Assert.assertNotNull(o);
        Assert.assertEquals(a.toString(), receivingAddress);
        String outpointStr = "8c322b170894442f94b81b49b2fce4e9855e98522bf974a7a98b5719bc608e45:0";
        OutPoint outPoint = OutPoint.fromString(outpointStr);
        Assert.assertEquals(outPoint.hash.toString(), "8c322b170894442f94b81b49b2fce4e9855e98522bf974a7a98b5719bc608e45");
        //a bit uncertain what height is, I assume it is height in block chain of transaction containing this output.
        //hardcoding to twelve
        UnspentTransactionOutput utxo = new UnspentTransactionOutput(outPoint, 12, 10000, o);
        System.out.println("Constructed utxo: " + utxo.toString());
        Assert.assertNotNull(utxo);
    }
}
