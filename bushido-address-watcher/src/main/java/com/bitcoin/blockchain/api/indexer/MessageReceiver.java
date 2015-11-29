package com.bitcoin.blockchain.api.indexer;

/**
 * Created by Jesion on 2015-02-02.
 */
import com.bitcoin.blockchain.api.IndexerCommand;
import com.bitcoin.blockchain.api.domain.V2Key;
import com.bitcoin.blockchain.api.domain.message.IMessage;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

public class MessageReceiver {

    @Autowired
    public AddressWatcher addressWatcher;

    @Autowired
    public TxBroadcaster txBroadcaster;

    public void receive(IMessage message) {
        if (message.getCommand().equals(IndexerCommand.WATCH_KEY)) {
            addressWatcher.addWatchingAddress((V2Key) message.getPayload());
        } else if (message.getCommand().equals(IndexerCommand.BROADCAST_TX)) {
            txBroadcaster.broadcastTx((String) message.getPayload());
        }
    }

    @Autowired
    public MessageSender sender;

    @Autowired
    public Queue wallet2indexerQueue;

    @Autowired
    public DirectExchange wallet2indexerExchange;

    @Autowired
    public AmqpAdmin amqpAdmin;

    @PostConstruct
    public void init() {
        Binding b = BindingBuilder.bind(wallet2indexerQueue).to(wallet2indexerExchange).with("");
        amqpAdmin.declareBinding(b);
    }
}

