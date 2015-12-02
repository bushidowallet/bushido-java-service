package com.bitcoin.blockchain.api.domain.message;

import com.bitcoin.blockchain.api.domain.Error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesion on 2015-03-24.
 */
public class ClientMessageBase extends Message implements Serializable {

    public ClientMessageBase() {

    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    private List<com.bitcoin.blockchain.api.domain.Error> errors;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void addError(Error error) {
        if (errors == null) {
            errors = new ArrayList<Error>();
        }
        this.errors.add(error);
    }

    public boolean hasError() {
        if (errors != null && errors.size() > 0) {
            return true;
        }
        return false;
    }

    public void setRawPayload(Object payload) {
        this.payload = payload;
    }
}
