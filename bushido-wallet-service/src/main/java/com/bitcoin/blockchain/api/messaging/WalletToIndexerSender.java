package com.bitcoin.blockchain.api.messaging;

import com.bitcoin.blockchain.api.domain.message.IMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Jesion on 2015-02-02.
 */
public class WalletToIndexerSender {

    @Autowired
    public RabbitTemplate ampqTemplate;

    public void send(IMessage message) {

        ampqTemplate.convertAndSend("wallet2indexerExchange", "", message);
    }
}
