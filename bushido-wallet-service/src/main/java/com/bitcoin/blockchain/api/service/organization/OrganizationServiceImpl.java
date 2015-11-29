package com.bitcoin.blockchain.api.service.organization;

import com.bitcoin.blockchain.api.domain.Error;
import com.bitcoin.blockchain.api.domain.Organization;
import com.bitcoin.blockchain.api.domain.PersistedOrganization;
import com.bitcoin.blockchain.api.domain.Response;
import com.bitcoin.blockchain.api.persistence.OrganizationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jesion on 2014-12-29.
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    public OrganizationDAO organizationDAO;

    public Response create(Organization o) {
        Response op = new Response();
        if (organizationDAO.hasOrganization(o.key) == false) {
            o.apiKey = getUUID();
            organizationDAO.create(new PersistedOrganization(o));
            op.setPayload(true);
        } else {
            op.addError(new Error(10));
        }
        return op;
    }

    public Response getAll() {
        List<PersistedOrganization> all = organizationDAO.getAll();
        List<Organization> allBase = new ArrayList<Organization>();
        for (PersistedOrganization organization : all) {
            allBase.add(organization.toBase());
        }
        return new Response(allBase);
    }

    public void delete(Organization o) {
        organizationDAO.delete(new PersistedOrganization(o));
    }

    private String getUUID() {
        return  UUID.randomUUID().toString();
    }
}
