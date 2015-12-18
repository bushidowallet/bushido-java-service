package com.bitcoin.blockchain.api.service.v2wallet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Jesion on 2015-12-18.
 */
public class UserPinTest {

    @Test
    public void testEncrypt() {
        String pin = "1131485833";
        Number pinNum = Integer.parseInt(pin);
        Assert.assertEquals(1131485833, pinNum);
    }
}
