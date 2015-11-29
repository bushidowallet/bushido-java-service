package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-02-25.
 */
public class ResponseBase implements Serializable {

    public ResponseBase() {

    }

    public ResponseBase(List<Error> errors) {
        this.errors = errors;
    }


    public void addError(Error error) {
        if (errors == null) {
            errors = new ArrayList<Error>();
        }
        this.errors.add(error);
    }

    private List<Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
