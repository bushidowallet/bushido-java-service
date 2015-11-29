package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;

/**
 * Created by Jesion on 2015-05-21.
 */
public class Exchange implements Serializable {

    public String id;
    public String accessorClass;

    public Exchange(String id, String accessorClass) {
        this.id = id;
        this.accessorClass = accessorClass;
    }

    public Exchange() {

    }
}
