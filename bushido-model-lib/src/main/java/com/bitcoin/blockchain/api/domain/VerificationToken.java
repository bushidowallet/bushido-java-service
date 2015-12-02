package com.bitcoin.blockchain.api.domain;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jesion on 2015-11-12.
 */
public class VerificationToken implements Serializable {

    private static final int DEFAULT_EXPIRY_TIME_IN_MINS = 60 * 24; //24 hours

    public String token;

    public Date creationDate;

    public Date expiryDate;

    public VerificationTokenType type;

    public String userId;

    public boolean secondFactorRequired;

    public boolean secondFactorComplete;

    public VerificationToken() {

    }

    public VerificationToken(VerificationTokenType type,
                             Date creationDate,
                             Date expiryDate,
                             String token,
                             String userId,
                             boolean secondFactorRequired,
                             boolean secondFactorComplete) {
        this.type = type;
        this.creationDate = creationDate;
        this.expiryDate = expiryDate;
        this.token = token;
        this.userId = userId;
        this.secondFactorRequired = secondFactorRequired;
        this.secondFactorComplete = secondFactorComplete;
    }

    public enum VerificationTokenType implements Serializable {
        lostPassword,
        emailVerification
    }

    public boolean hasExpired() {
        final DateTime expiry = new DateTime(expiryDate);
        return expiry.isBeforeNow();
    }

    private static Date calculateExpiryDate(int minsFromNow) {
        final DateTime now = new DateTime();
        return now.plusMinutes(minsFromNow).toDate();
    }

    public static VerificationToken create(VerificationTokenType type, String userId, boolean secondFactorRequired) {
        return new VerificationToken(type,
                new Date(),
                calculateExpiryDate(DEFAULT_EXPIRY_TIME_IN_MINS),
                UUID.randomUUID().toString(),
                userId,
                secondFactorRequired,
                false
        );
    }
}
