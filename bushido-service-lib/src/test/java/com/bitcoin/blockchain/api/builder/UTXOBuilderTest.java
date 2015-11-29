package com.bitcoin.blockchain.api.builder;

import com.bccapi.bitlib.model.ScriptOutputStandard;
import com.bccapi.bitlib.model.UnspentTransactionOutput;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jesion on 2015-03-16.
 */
public class UTXOBuilderTest {

    @Test
    public void testBuild() throws Exception {

        UTXOBuilder builder = new UTXOBuilder();
        UnspentTransactionOutput utxo = builder.build("8c322b170894442f94b81b49b2fce4e9855e98522bf974a7a98b5719bc608e45",
                12,
                "76a9146634adac3b80c8095797dbf90508c479b5fcedf088ac",
                0,
                10000
        );
        Assert.assertNotNull(utxo);
        Assert.assertTrue(utxo.script instanceof ScriptOutputStandard);
    }
}
