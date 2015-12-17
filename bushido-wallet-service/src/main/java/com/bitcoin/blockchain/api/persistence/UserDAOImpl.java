package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedUser;
import com.bitcoin.blockchain.api.util.UserUtil;
import com.bushidowallet.core.crypto.util.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Jesion on 2014-12-28.
 */
@Service("userDAO")
public class UserDAOImpl implements UserDAO {

    public static String USER_COLLECTION = "user";

    @Autowired
    public MongoOperations mongoOps;

    @Autowired
    public MessageDigestPasswordEncoder passwordEncoder;

    @Autowired
    public SaltSource saltSource;

    public void create(PersistedUser user) {
        this.mongoOps.insert(user, USER_COLLECTION);
    }

    public void update(PersistedUser user) {
        final Query query = new Query(Criteria.where("id").is(user.getId()));
        final Update update = new Update();
        update.set("roles", user.roles);
        update.set("username", user.username);
        this.mongoOps.upsert(query, update, PersistedUser.class, USER_COLLECTION);
    }

    public void delete(PersistedUser user) {
        final Query query = new Query(Criteria.where("id").is(user.getId()));
        this.mongoOps.remove(query, PersistedUser.class, USER_COLLECTION);
    }

    public PersistedUser get(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return this.mongoOps.findOne(query, PersistedUser.class, USER_COLLECTION);
    }

    public PersistedUser getByUserIdOrEmail(String userIdOrEmail) {
        Query query = new Query(Criteria.where("username").is(userIdOrEmail));
        PersistedUser u = this.mongoOps.findOne(query, PersistedUser.class, USER_COLLECTION);
        if (u == null) {
            query = new Query(Criteria.where("email").is(userIdOrEmail));
            u = this.mongoOps.findOne(query, PersistedUser.class, USER_COLLECTION);
        }
        return u;
    }

    public PersistedUser getById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return this.mongoOps.findOne(query, PersistedUser.class, USER_COLLECTION);
    }

    public PersistedUser getByEmail(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return this.mongoOps.findOne(query, PersistedUser.class, USER_COLLECTION);
    }

    public void updatePassword(PersistedUser user, String password) {
        final Query query = new Query(Criteria.where("id").is(user.getId()));
        final Update update = new Update();
        update.set("passwordHash", passwordEncoder.encodePassword(password, saltSource.getSalt(UserUtil.toUser(user))));
        this.mongoOps.upsert(query, update, PersistedUser.class, USER_COLLECTION);
    }

    public void setPinHash(String username, String pinHash) throws Exception {
        final PersistedUser u = get(username);
        if (u != null) {
            final Query query = new Query(Criteria.where("id").is(u.getId()));
            final Update update = new Update();
            update.set("pinHash", pinHash);
            this.mongoOps.upsert(query, update, PersistedUser.class, USER_COLLECTION);
        } else {
            throw new Exception("Could not find user with username: " + username);
        }
    }

    public UserInfo isValid(String userIdOrEmail, String password) {
        final PersistedUser u = getByUserIdOrEmail(userIdOrEmail);
        if (u != null) {
            return new UserInfo(passwordEncoder.isPasswordValid(u.passwordHash, password, saltSource.getSalt(UserUtil.toUser(u))), u);
        }
        return new UserInfo(false, u);
    }

    public UserInfo isValid(String userIdOrEmail, String password, String pin) {
        final PersistedUser u = getByUserIdOrEmail(userIdOrEmail);
        if (u != null) {
            final boolean passwordValid = passwordEncoder.isPasswordValid(u.passwordHash, password, saltSource.getSalt(UserUtil.toUser(u)));
            String generatedPinHash = "";
            try {
                generatedPinHash = createPinHash(pin);
            } catch (Exception e) {

            }
            final boolean pinValid = generatedPinHash.equals(u.pinHash);
            final boolean uValid = passwordValid && pinValid == true;
            return new UserInfo(uValid, u);
        }
        return new UserInfo(false, u);
    }

    private String createPinHash(String pin) throws Exception {
        return ByteUtil.toHex(new com.bushidowallet.core.bitcoin.bip32.Hash(pin).hash());
    }

    public class UserInfo {

        public boolean valid;

        public PersistedUser user;

        public UserInfo(boolean valid, PersistedUser user) {
            this.valid = valid;
            this.user = user;
        }
    }

    public boolean hasUser(String username) {
        return get(username) != null ? true : false;
    }

    public boolean hasUserWithEmail(String email) {
        return getByEmail(email) != null ? true : false;
    }

    public List<PersistedUser> getAll() {
        return this.mongoOps.findAll(PersistedUser.class, USER_COLLECTION);
    }

    public void drop() {
        this.mongoOps.dropCollection(USER_COLLECTION);
    }
}
