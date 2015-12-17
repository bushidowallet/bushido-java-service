package com.bitcoin.blockchain.api.util;

import com.bitcoin.blockchain.api.domain.User;
import com.bitcoin.blockchain.api.domain.V2WalletDescriptor;
import com.bitcoin.blockchain.api.domain.V2WalletSetting;

/**
 * Created by Jesion on 2015-06-26.
 */
public class SecurityUtil {

    public static V2WalletDescriptor process(V2WalletDescriptor wallet) {
        V2WalletDescriptor c = wallet.clone();
        int k = -1;
        for (int i = 0; i < c.settings.size(); i++) {
            if (c.settings.get(i).key.equals(V2WalletSetting.PASSPHRASE)) {
                k = i;
            }
        }
        if (k >= 0) {
            c.settings.remove(k);
        }
        return c;
    }

    public static void process(User user) {
        user.salt = null;
        user.passwordHash = null;
        user.pinHash = null;
    }
}
