package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Jesion on 2015-10-23.
 */
@Document
public class PersistedTopUpTransaction extends TopUpTransaction {

    @Id
    private String id;

    public PersistedTopUpTransaction() {
        super();
    }

    public PersistedTopUpTransaction(String userId,
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
        super(userId,
            walletKey,
            date,
            gateway,
            gatewayTransactionId,
            currency,
            amount,
            control,
            status,
            widthdrawalAmount,
            commission,
            originalAmount
        );
    }

    public String getId() {
        return id;
    }
}
