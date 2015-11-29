package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Jesion on 2015-02-26.
 */
@Document
public class PersistedOrganization extends Organization {

    @Id
    private String id;

    public PersistedOrganization() {
        super();
    }

    public PersistedOrganization(String key, String name, String apiKey) {
        super(key, name, apiKey);
    }

    public PersistedOrganization(Organization o) {
        this(o.key, o.name, o.apiKey);
    }

    public Organization toBase() {
        return new Organization(this.key, this.name, this.apiKey);
    }
}
