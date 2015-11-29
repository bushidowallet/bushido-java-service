package com.bitcoin.blockchain.api.service.v2wallet;

import com.bitcoin.blockchain.api.Command;
import com.bitcoin.blockchain.api.domain.message.ClientMessage;
import com.bitcoin.blockchain.api.domain.message.Message;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesion on 2014-12-07.
 */
public class V2WalletMessageRouter {

    @Autowired
    public RabbitTemplate ampqTemplate;

    @Autowired
    public RabbitAdmin ampqAdmin;

    @Autowired
    public Queue v2qupdates;

    @Autowired
    public TopicExchange v2eupdates;

    private Map<String,String> keys = new HashMap<String, String>();

    @PostConstruct
    public void init() {

    }

    public void register(String key) {
        BindingBuilder.bind(v2qupdates).to(v2eupdates).with(key);
        keys.put(key, "");
    }

    public void sendUpdate(String routingKey, Message outMessage) {
        ampqTemplate.convertAndSend("v2e-wallet-updates", routingKey, outMessage);
    }

    public void ping() {
        for (Map.Entry<String, String> entry : keys.entrySet()) {
            sendUpdate(entry.getKey(), new ClientMessage(null, entry.getKey(), Command.HEARTBEAT));
        }
    }
}
