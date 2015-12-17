package com.bitcoin.blockchain.api.data;

import com.bitcoin.blockchain.api.domain.PersistedUser;
import com.bitcoin.blockchain.api.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jesion on 2014-12-28.
 */
public class Users {

    @Autowired
    public MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    public SaltSource saltSource;

    @Value("${app.chainuser}")
    private String chainUser;

    @Value("${app.chainpass}")
    private String chainPass;

    private PersistedUser chainUserObj;

    public List<PersistedUser> getAll() {
        ArrayList<PersistedUser> users = new ArrayList<PersistedUser>();
        users.add(chainUserObj);
        try {
            chainUserObj.passwordHash = passwordEncoder.encodePassword(chainPass, saltSource.getSalt(UserUtil.toUser(chainUserObj)));
        } catch (Exception e) {

        }
        return users;
    }

    @PostConstruct
    public void init() {
        this.chainUserObj = new PersistedUser(chainUser, null, Arrays.asList("ROLE_USER"), "individuals", "fake@mail.com");
    }
}
