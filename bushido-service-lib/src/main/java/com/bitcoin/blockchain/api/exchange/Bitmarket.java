package com.bitcoin.blockchain.api.exchange;

import com.bitcoin.blockchain.api.domain.Exchange;
import com.bitcoin.blockchain.api.domain.ExchangeData;
import com.bitcoin.blockchain.api.domain.ExchangeInfo;
import com.bitcoin.blockchain.api.domain.Instruments;
import org.json.JSONObject;

/**
 * Created by Jesion on 2015-05-21.
 */
public class Bitmarket {

    public Bitmarket() {

    }

    public ExchangeInfo getInfo() throws Exception {
        final Exchange exchange = new Exchange("bitmarket.pl", null);
        final ExchangeData data = getBTCPLN();
        return new ExchangeInfo(exchange, data);
    }

    public ExchangeData getBTCPLN() throws Exception {
        final JSONObject o = JSONReader.readJsonFromUrl("https://www.bitmarket.pl/json/BTCPLN/ticker.json");
        final ExchangeData data = new ExchangeData(Instruments.btcpln,
                o.getDouble("volume"),
                o.getDouble("high"),
                o.getDouble("last"),
                o.getDouble("low"),
                o.getDouble("vwap"),
                o.getDouble("ask"),
                o.getDouble("bid"));
        return data;
    }
}
