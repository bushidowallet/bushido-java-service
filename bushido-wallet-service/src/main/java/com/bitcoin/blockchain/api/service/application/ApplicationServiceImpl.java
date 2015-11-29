package com.bitcoin.blockchain.api.service.application;

import com.bitcoin.blockchain.api.application.ApplicationConfig;
import com.bitcoin.blockchain.api.data.Organizations;
import com.bitcoin.blockchain.api.data.Users;
import com.bitcoin.blockchain.api.domain.*;
import com.bitcoin.blockchain.api.persistence.*;
import com.bitcoin.blockchain.api.service.v2wallet.V2WalletService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Jesion on 2014-11-30.
 */
@Service
public class ApplicationServiceImpl implements ApplicationService, ApplicationContextAware {

    @Autowired
    public ApplicationConfig config;

    @Autowired
    public V2WalletDAO v2WalletDAO;

    @Autowired
    public V2WalletKeyDAO v2WalletKeyDAO;

    @Autowired
    public V2WalletService v2WalletService;

    @Autowired
    public UserDAO userDAO;

    @Autowired
    public OrganizationDAO organizationDAO;

    @Autowired
    public TransactionDAO transactionDAO;

    @Autowired
    public TransactionOutpointDAO outpointDAO;

    @Autowired
    public Users usersPredefined;

    private ApplicationContext ctx;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.ctx = context;
    }

    public void resetDb() throws Exception {
        v2WalletDAO.drop();
        v2WalletKeyDAO.drop();
        userDAO.drop();
        organizationDAO.drop();
        transactionDAO.drop();
        outpointDAO.drop();
    }

    @PostConstruct
    public void init() {
        setup();
    }

    private void setup() {
        createOrganizations();
        createUsers();
    }

    private void createOrganizations() {
        List<PersistedOrganization> organizations = Organizations.getAll();
        for (PersistedOrganization o : organizations) {
            if (organizationDAO.hasOrganization(o.key) == false) {
                organizationDAO.create(o);
            }
        }
    }

    private void createUsers() {
        List<PersistedUser> users = usersPredefined.getAll();
        for (PersistedUser user : users) {
            if (userDAO.hasUser(user.username) == false) {
                userDAO.create(user);
            }
        }
    }
}
