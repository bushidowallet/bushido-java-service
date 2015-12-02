package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesion on 2015-05-21.
 */
public class ExchangeModel implements Serializable {

    public Map<String, ExchangeInfo> exchanges;

    public ExchangeModel() {

        exchanges = new HashMap<String, ExchangeInfo>();
    }

    public void addInfo(String key, ExchangeInfo info) {
        exchanges.put(key, info);
    }

    public void updateData(String key, ExchangeData data) {
        ExchangeInfo existingInfo = exchanges.get(key);
        existingInfo.data.vwap = data.vwap;
        existingInfo.data.instrument = data.instrument;
        existingInfo.data.setBid(data.bid);
        existingInfo.data.ask = data.ask;
        existingInfo.data.high = data.high;
        existingInfo.data.volume = data.volume;
        existingInfo.data.low = data.low;
        existingInfo.data.last = data.last;
    }
}
