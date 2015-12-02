package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Jesion on 2015-10-23.
 */
public class TopUpTransaction implements Serializable {

    public String userId;
    public String walletKey;
    public Date date;
    public String gateway;
    public String gatewayTransactionId;
    public String currency;
    public BigDecimal amount;
    public String control;
    public String status;
    public BigDecimal widthdrawalAmount;
    public BigDecimal commission;
    public BigDecimal originalAmount;

    public TopUpTransaction() {

    }

    public TopUpTransaction(String userId,
                            String walletKey,
                            Date date,
                            String gateway,
                            String gatewayTransactionId,
                            String currency,
                            BigDecimal amount,
                            String control,
                            String status,
                            BigDecimal widthdrawalAmount,
                            BigDecimal commission,
                            BigDecimal originalAmount) {

        this.userId = userId;
        this.walletKey = walletKey;
        this.date = date;
        this.gateway = gateway;
        this.gatewayTransactionId = gatewayTransactionId;
        this.currency = currency;
        this.amount = amount;
        this.control = control;
        this.status = status;
        this.widthdrawalAmount = widthdrawalAmount;
        this.commission = commission;
        this.originalAmount = originalAmount;
    }
}
