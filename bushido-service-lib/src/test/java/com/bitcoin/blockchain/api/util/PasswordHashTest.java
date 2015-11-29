package com.bitcoin.blockchain.api.util;

import org.junit.Test;

/**
 * Created by Jesion on 2015-06-20.
 */
public class PasswordHashTest {

    @Test
    public void testCreateHash() {
        try
        {
            // Print out 10 hashes
            for(int i = 0; i < 10; i++)
                System.out.println(PasswordHash.createHash("p\r\nassw0Rd!"));

            // Test password validation
            boolean failure = false;
            System.out.println("Running tests...");
            for(int i = 0; i < 100; i++)
            {
                String password = ""+i;
                String hash = PasswordHash.createHash(password);
                String secondHash = PasswordHash.createHash(password);
                if(hash.equals(secondHash)) {
                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
                    failure = true;
                }
                String wrongPassword = ""+(i+1);
                if(PasswordHash.validatePassword(wrongPassword, hash)) {
                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
                    failure = true;
                }
                if(!PasswordHash.validatePassword(password, hash)) {
                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
                    failure = true;
                }
            }
            if(failure) {
                System.out.println("TESTS FAILED!");
            } else {
                System.out.println("TESTS PASSED!");
            }
        }
        catch(Exception ex)
        {
            System.out.println("ERROR: " + ex);
        }
    }
}
