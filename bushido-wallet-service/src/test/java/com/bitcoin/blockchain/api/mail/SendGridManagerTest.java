package com.bitcoin.blockchain.api.mail;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Jesion on 2015-06-20.
 */
public class SendGridManagerTest {

    @Test
    @Ignore
    public void testGetInfo() throws Exception {

        SendGridManager manager = new SendGridManager();
        manager.send("robert@riaforge.co.uk", "yesionuk@gmail.com", "Subject here", "Content here");

        Assert.assertTrue(true);
    }
}
