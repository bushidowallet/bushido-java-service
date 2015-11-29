package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Jesion on 2015-02-26.
 */
@Document
public class PersistedSetting extends Setting {

    @Id
    private String id;

    public PersistedSetting(String key, Object value) {

        this.key = key;
        this.value = value;
    }

    public PersistedSetting() {

    }
}
