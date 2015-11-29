package com.bitcoin.blockchain.api.exchange;

import com.bitcoin.blockchain.api.domain.ExchangeInfo;
import com.bitcoin.blockchain.api.domain.Instruments;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Jesion on 2015-05-21.
 */
public class BitmarketTest {

    @Test
    @Ignore
    public void testGetInfo() throws Exception {

        ExchangeInfo info = new Bitmarket().getInfo();
        Assert.assertEquals(Instruments.btcpln, info.data.instrument);
    }
}
