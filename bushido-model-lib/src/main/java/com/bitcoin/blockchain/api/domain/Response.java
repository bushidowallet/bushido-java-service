package com.bitcoin.blockchain.api.domain;

import java.util.List;

/**
 * Created by Jesion on 2014-12-25.
 */
public class Response extends ResponseBase {

    public Response() {
        super();
    }

    public Response(List<Error> errors, Object payload) {
        super(errors);
        this.payload = payload;
    }

    public Response(Object payload) {
        super();
        this.payload = payload;
    }

    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
