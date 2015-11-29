package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jesion on 2014-12-28.
 */
@Service("organizationDAO")
public class OrganizationDAOImpl implements OrganizationDAO {

    public static String ORGANIZATION_COLLECTION = "organization";

    @Autowired
    public MongoOperations mongoOps;

    public void create(PersistedOrganization o) {
        this.mongoOps.insert(o, ORGANIZATION_COLLECTION);
    }

    public void delete(PersistedOrganization o) {
        final Query query = new Query(Criteria.where("key").is(o.key));
        this.mongoOps.remove(query, PersistedOrganization.class, ORGANIZATION_COLLECTION);
    }

    public PersistedOrganization get(String key) {
        Query query = new Query(Criteria.where("key").is(key));
        return this.mongoOps.findOne(query, PersistedOrganization.class, ORGANIZATION_COLLECTION);
    }

    public boolean hasOrganization(String key) {
        return get(key) != null ? true : false;
    }

    public List<PersistedOrganization> getAll() {
        return this.mongoOps.findAll(PersistedOrganization.class, ORGANIZATION_COLLECTION);
    }

    public void drop() {
        this.mongoOps.dropCollection(ORGANIZATION_COLLECTION);
    }
}
