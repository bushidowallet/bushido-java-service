package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-01-20.
 */
public class Organization implements Serializable {

    public String key;

    public String name;

    public String apiKey;

    public Organization(String key, String name, String apiKey) {
        this.key = key;
        this.name = name;
        this.apiKey = apiKey;
    }

    public Organization() {

    }
}
