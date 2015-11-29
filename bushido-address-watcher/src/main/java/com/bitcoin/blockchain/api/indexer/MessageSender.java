package com.bitcoin.blockchain.api.indexer;

import com.bitcoin.blockchain.api.domain.message.IMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Jesion on 2015-02-03.
 */
public class MessageSender {

    @Autowired
    public RabbitTemplate ampqTemplate;

    public void send(IMessage message) {
        ampqTemplate.convertAndSend("indexer2walletExchange", "", message);
    }
}
