package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.domain.message.ClientMessageBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Jesion on 2015-01-09.
 */
public class V2WalletMessageReceiver {

    @Autowired
    public V2WalletService service;

    public void receiveMessage(ClientMessageBase message) {
        service.messageToWallet(message);
    }
}
