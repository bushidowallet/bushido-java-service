package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedOrganization;

import java.util.List;

/**
 * Created by Jesion on 2015-01-20.
 */
public interface OrganizationDAO {

    public void create(PersistedOrganization o);

    public void delete(PersistedOrganization o);

    public PersistedOrganization get(String key);

    public boolean hasOrganization(String key);

    public List<PersistedOrganization> getAll();

    public void drop();
}
