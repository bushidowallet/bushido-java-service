package com.bitcoin.blockchain.api.exchange;

import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.service.v2wallet.V2Wallet;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jesion on 2015-05-21.
 */
public class ExchangeDataAccessor {

    @Autowired
    public ExchangeModel model;

    @Autowired
    public V2WalletRegistry wallets;

    @PostConstruct
    public void init() {
        ExchangeData data = new ExchangeData();
        data.addObserver(new BTCPLNBidObserver());
        data.instrument = Instruments.btcpln;
        ExchangeInfo info = new ExchangeInfo(Exchanges.bitmarket, data);
        model.addInfo(Exchanges.bitmarket.id, info);
    }

    public void getData() throws Exception {
        Class<?> clazz = Class.forName(Exchanges.bitmarket.accessorClass);
        Bitmarket accessor = (Bitmarket) clazz.newInstance();
        ExchangeData data = accessor.getBTCPLN();
        model.updateData(Exchanges.bitmarket.id, data);
        System.out.println("Bitmarket.pl price fetched: " + data.bid);
    }

    private class BTCPLNBidObserver implements Observer {
        public void update(Observable obs, Object x) {
            ExchangeData data = model.exchanges.get(Exchanges.bitmarket.id).data;
            double btcplnbid = data.getBid();
            System.out.println("Notify about BTCPLN (bid) change to: " + btcplnbid);
            //first check what wallets are active, and find the once that have interest in BTCPLN
            List<V2Wallet> openWallets = wallets.getWallets();
            for (int i = 0; i < openWallets.size(); i++) {
                V2Wallet wallet = openWallets.get(i);
                V2WalletDescriptor descriptor = wallet.getDescriptor(false);
                V2WalletSetting instruments = descriptor.getSetting(V2WalletSetting.INSTRUMENTS);
                if (instruments != null) {
                    String[] instr = ((String) instruments.value).split(",");
                    for (int j = 0; j < instr.length; j++) {
                        String instrument = instr[j];
                        if (instrument.equals(Instruments.btcpln.id)) {
                            //wallet receives this update..
                            System.out.println("Wallet " + wallet.getDescriptor(false).key + " will receive instrument update BTCPLN (bid): " + data.getBid());
                            wallet.instrumentUpdate(Instruments.btcpln, data);
                        }
                    }
                }
            }
        }
    }
}
