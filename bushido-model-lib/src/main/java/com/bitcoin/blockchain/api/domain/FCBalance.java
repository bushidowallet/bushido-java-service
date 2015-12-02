package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jesion on 2015-10-26.
 */
public class FCBalance implements Serializable {

    public String currency;

    public BigDecimal balance;

    public FCBalance() {

    }

    public FCBalance(String currency, BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;
    }
}
