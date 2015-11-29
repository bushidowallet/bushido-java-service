package com.authy;

import com.authy.api.Hash;
import com.authy.api.Token;
import com.authy.api.Tokens;
import com.authy.api.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesion on 2015-06-26.
 */
public class AuthyApiClientTest {

    private AuthyApiClient client = new AuthyApiClient("authyAPIKey");

    @Test
    @Ignore
    public void testCreateUser() {

        User u = client.getUsers().createUser("robert@riaforge.co.uk", "787651234", "48");
        Assert.assertNotNull(u);
        Assert.assertEquals(7020094, u.getId());
    }

    @Test
    @Ignore
    public void testRequestToken() {

        Hash r = client.getUsers().requestSms(7020094);
        Assert.assertTrue(r.isSuccess());
    }

    @Test
    @Ignore
    public void testRequestTokenSMS() {
        Map<String, String> o = new HashMap<String, String>();
        o.put("force", "true");
        Hash r = client.getUsers().requestSms(7020094, o);
        Assert.assertTrue(r.isSuccess());
    }

    @Test
    @Ignore
    public void testAuth() {

        Tokens tokens = client.getTokens();
        Token t = tokens.verify(7020094, "9585315");
        Assert.assertTrue(t.isOk());
    }
}
