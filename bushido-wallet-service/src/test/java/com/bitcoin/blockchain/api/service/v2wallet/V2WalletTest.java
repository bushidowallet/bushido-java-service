package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.domain.PersistedTransaction;

import org.junit.Test;

/**
 * Created by Jesion on 2015-06-25.
 */
public class V2WalletTest {

    @Test
    public void testListenForUpdates() {

        V2Wallet wallet = new V2Wallet();
        PersistedTransaction tx = new PersistedTransaction();
        tx.hash = "c2bdd90471ac397ff00ea3a002048726a091da2191e958e817e8830d67accc6d";
        wallet.listenForUpdates(tx);
    }
}
