package com.bitcoin.blockchain.api.domain;

/**
 * Created by Jesion on 2015-06-02.
 */
public class RegOrganization extends Organization {

    public String regCode;

    public RegOrganization(String key, String name, String apiKey, String regCode) {

        super(key, name, apiKey);

        this.regCode = regCode;
    }

    public RegOrganization() {

    }
}
