package com.bitcoin.blockchain.api.persistence;

import com.bitcoin.blockchain.api.domain.PersistedUser;
import com.bitcoin.blockchain.api.domain.UserPin;

import java.util.List;

/**
 * Created by Jesion on 2014-12-28.
 */
public interface UserDAO {

    public void create(PersistedUser user);

    public void update(PersistedUser user);

    public void delete(PersistedUser user);

    public UserDAOImpl.UserInfo isValid(String userIdOrEmail, String password);

    public PersistedUser getByUserIdOrEmail(String userIdOrEmail);

    public PersistedUser get(String userName);

    public PersistedUser getByEmail(String email);

    public void updatePassword(PersistedUser user, String password);

    public void setPinHash(String username, String pinHash) throws Exception;

    public PersistedUser getById(String id);

    public boolean hasUser(String userName);

    public boolean hasUserWithEmail(String email);

    public List<PersistedUser> getAll();

    public void drop();
}
