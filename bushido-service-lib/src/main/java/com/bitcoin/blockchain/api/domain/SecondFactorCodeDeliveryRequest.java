package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-11-15.
 */
public class SecondFactorCodeDeliveryRequest extends TokenRequest {

    public boolean enforceSms;

    public SecondFactorCodeDeliveryRequest() {
        super();
    }

    public SecondFactorCodeDeliveryRequest(String token, boolean enforceSms) {
        super(token);
        this.enforceSms = enforceSms;
    }
}
