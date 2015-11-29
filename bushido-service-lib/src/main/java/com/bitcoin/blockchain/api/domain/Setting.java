package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2014-12-18.
 */
public class Setting implements Serializable {

    public String key;

    public Object value;

    public Setting(String key, Object value) {

        this.key = key;
        this.value = value;
    }

    public Setting() {

    }
}
