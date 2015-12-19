package com.bitcoin.blockchain.api.service.v2wallet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.Security;

/**
 * Created by Jesion on 2015-12-17.
 */
public class V2WalletCryptoTest {

    private String passphraseHash = "85e14868018cb94f0436d7dc58fad98ef40c66da923b3b63933247195a8747fb";
    private String encryptedPassphraseHash = "ZEzH39MvCD7UQxJ4kfxfqmZxrJmeiFtlvFqo1G0wDRE/qoYBt/hotCh49epPHOvk1BmLOvSvaMZTUiAQU/B30YlZI/2Uo1tgua77WRg3q40=";
    private String pinSalt = "SatoshiNakamotoDeservesTheNobelPrizeLikeNoOneElse";
    private String pin = "4031";

    @BeforeClass
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testEncrypt() {
        String cipherText = V2WalletCrypto.encrypt(passphraseHash, true, pin, pinSalt);
        System.out.println("Encrypted: " + cipherText);
        Assert.assertNotNull(cipherText);
        Assert.assertEquals(encryptedPassphraseHash, cipherText);
    }

    @Test
    public void testDecrypt() {
        String decryptedPassphraseHash = V2WalletCrypto.decrypt(encryptedPassphraseHash, true, pin, pinSalt);
        Assert.assertEquals(passphraseHash, decryptedPassphraseHash);
    }
}
