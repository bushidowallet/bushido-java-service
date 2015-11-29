package com.bitcoin.blockchain.api.indexer;

import com.bushidowallet.core.crypto.util.ByteUtil;
import org.bitcoinj.core.*;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Jesion on 2015-03-19.
 */
public class TxBroadcaster {

    private NetworkParameters params;
    private PeerGroup peerGroup;

    @PostConstruct
    public void init() {
        params = MainNetParams.get();
        peerGroup = new PeerGroup(params, null);
        peerGroup.setMaxConnections(4);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.addEventListener(new AbstractPeerEventListener() {
            @Override
            public void onPeerConnected(final Peer peer, int peerCount) {
                System.out.println("Peer connected: " + peer.getAddress().toString());
            }

            @Override
            public void onPeerDisconnected(final Peer peer, int peerCount) {
                System.out.println("Peer disconnected: " + peer.getAddress().toString());
            }
        });
        peerGroup.startAsync();
    }

    @PreDestroy
    public void destroy() throws Exception{
        peerGroup.stopAsync();
        Thread.sleep(5000);
    }

    public void broadcastTx(String txHex) {
        byte[] payload = ByteUtil.fromHex(txHex);
        Transaction tx = new Transaction(params, payload);
        System.out.println("Broadcasting TX: " + tx.getHashAsString());
        for (int i = 0; i < peerGroup.getConnectedPeers().size(); i++) {
            peerGroup.getConnectedPeers().get(i).sendMessage(tx);
        }
    }
}
