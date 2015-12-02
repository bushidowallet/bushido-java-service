package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.Observable;

/**
 * Created by Jesion on 2015-05-21.
 */
public class ExchangeData extends Observable implements Serializable {

    public ExchangeData() {

    }

    public Instrument instrument;
    public double volume;
    public double high;
    public double last;
    public double low;
    public double vwap;
    public double ask;
    public double bid;

    public ExchangeData(Instrument instrument,
                        double volume,
                        double high,
                        double last,
                        double low,
                        double vwap,
                        double ask,
                        double bid) {
        this.instrument = instrument;
        this.volume = volume;
        this.high = high;
        this.last = last;
        this.low = low;
        this.vwap = vwap;
        this.ask = ask;
        this.bid = bid;
    }

    public void setBid(double bid) {
        if (this.bid != bid) {
            this.bid = bid;
            setChanged();
            notifyObservers();
        }
    }

    public double getBid() {
        return bid;
    }
}
