package com.bitcoin.blockchain.api.service.organization;

import com.bitcoin.blockchain.api.domain.Organization;
import com.bitcoin.blockchain.api.domain.Response;

/**
 * Created by Jesion on 2015-01-20.
 */
public interface OrganizationService {

    public Response create(Organization user);

    public Response getAll();

    public void delete(Organization user);
}
