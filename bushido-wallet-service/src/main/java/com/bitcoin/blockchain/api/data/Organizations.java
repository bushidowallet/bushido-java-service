package com.bitcoin.blockchain.api.data;

import com.bitcoin.blockchain.api.domain.PersistedOrganization;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jesion on 2015-01-20.
 */
public class Organizations {

    public static List<PersistedOrganization> getAll() {
        ArrayList<PersistedOrganization> organizations = new ArrayList<PersistedOrganization>();
        organizations.add(individuals);
        return organizations;
    }

    public static PersistedOrganization individuals = new PersistedOrganization("individuals", "Individuals", UUID.randomUUID().toString());
}
