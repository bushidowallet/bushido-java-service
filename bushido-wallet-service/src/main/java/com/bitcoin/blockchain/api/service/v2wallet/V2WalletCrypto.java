package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.domain.UserPin;
import com.bushidowallet.core.crypto.symmetric.aes.AES;
import com.bushidowallet.core.crypto.symmetric.key.DerivedKey;

import javax.crypto.SecretKey;

/**
 * Created by Jesion on 2015-12-17.
 */
public class V2WalletCrypto {

    public static String decrypt(String passphraseHash, boolean pinEnabled, UserPin pin, String pinSalt) {
        if (pinEnabled == false) {
            return passphraseHash;
        } else {
            final DerivedKey symmetricKey = new DerivedKey(pin.toString(), pinSalt.getBytes(), 128);
            try {
                symmetricKey.generate();
                final SecretKey secretKey = symmetricKey.getKey();
                final AES cipher = new AES();
                return cipher.decrypt(passphraseHash, secretKey);
            } catch (Exception e) {
                System.out.println("Error while decrypting passphrase hash " + e.toString());
            }
        }
        return null;
    }

    public static String encrypt(String passphraseHash, boolean pinEnabled, Number pin, String pinSalt) {
        if (pinEnabled == false) {
            return passphraseHash;
        } else {
            final DerivedKey symmetricKey = new DerivedKey(pin.toString(), pinSalt.getBytes(), 128);
            try {
                symmetricKey.generate();
                final SecretKey secretKey = symmetricKey.getKey();
                final AES cipher = new AES();
                return cipher.encrypt(passphraseHash, secretKey);
            } catch (Exception e) {
                System.out.println("Error while encrypting passphrase hash " + e.toString());
            }
        }
        return null;
    }
}
