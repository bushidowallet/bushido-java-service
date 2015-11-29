package com.bitcoin.blockchain.api.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Jesion on 2015-10-25.
 */
public class TopUpNotification {

    public int accountId;
    public String id;
    public String status;
    public String type;
    public BigDecimal amount;
    public String currency;
    public BigDecimal widthdrawalAmount;
    public BigDecimal commission;
    public BigDecimal originalAmount;
    public String originalCurrency;
    public Date dateTime;
    public String relatedNumber;
    public String control;
    public String description;
    public String email;
    public String pInfo;
    public String pEmail;
    public String channel;
    public String channelCountry;
    public String geoIpCountry;
    public String signature;

    public TopUpNotification() {

    }

    public TopUpNotification(int accountId,
            String id,
            String status,
            String type,
            BigDecimal amount,
            String currency,
            BigDecimal widthdrawalAmount,
            BigDecimal commission,
            BigDecimal originalAmount,
            String originalCurrency,
            Date dateTime,
            String relatedNumber,
            String control,
            String description,
            String email,
            String pInfo,
            String pEmail,
            String channel,
            String channelCountry,
            String geoIpCountry,
            String signature) {

        this.accountId = accountId;
        this.id = id;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.widthdrawalAmount = widthdrawalAmount;
        this.commission = commission;
        this.originalAmount = originalAmount;
        this.originalCurrency = originalCurrency;
        this.dateTime = dateTime;
        this.relatedNumber = relatedNumber;
        this.control = control;
        this.description = description;
        this.email = email;
        this.pInfo = pInfo;
        this.pEmail = pEmail;
        this.channel = channel;
        this.channelCountry = channelCountry;
        this.geoIpCountry = geoIpCountry;
        this.signature = signature;
    }
}
