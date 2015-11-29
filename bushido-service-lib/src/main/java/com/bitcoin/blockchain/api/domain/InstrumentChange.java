package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-05-22.
 */
public class InstrumentChange implements Serializable {

    public Instrument instrument;

    public ExchangeData data;

    public InstrumentChange() {

    }

    public InstrumentChange(Instrument instrument, ExchangeData data) {
        this.instrument = instrument;
        this.data = data;
    }
}
