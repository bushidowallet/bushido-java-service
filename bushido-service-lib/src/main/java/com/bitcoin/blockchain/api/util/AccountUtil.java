package com.bitcoin.blockchain.api.util;

import com.bitcoin.blockchain.api.domain.V2WalletAccount;
import com.bitcoin.blockchain.api.domain.V2WalletDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jesion on 2015-02-25.
 */
public class AccountUtil {

    public static synchronized int getNextAccount(V2WalletDescriptor wallet) {
        List<Integer> a = new ArrayList<Integer>();
        if (wallet.hasAccount()) {
            for (V2WalletAccount accountDTO : wallet.accounts) {
                a.add(accountDTO.account);
            }
        }
        Collections.sort(a);
        return a.get(a.size() - 1) + 1;
    }
}
