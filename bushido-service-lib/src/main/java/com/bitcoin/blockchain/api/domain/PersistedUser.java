package com.bitcoin.blockchain.api.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Jesion on 2015-02-26.
 */
@Document
public class PersistedUser extends User {

    @Id
    private String id;

    public PersistedUser() {
        super();
    }

    public PersistedUser(String userName,
                         String password,
                         List<String> roles,
                         String organization,
                         String email) {
        super(userName, password, roles, organization, email);
    }

    public PersistedUser(String userName,
                         String password,
                         List<String> roles,
                         String organization,
                         String email,
                         String phone,
                         String countryCode) {
        super(userName, password, roles, organization, email);
        this.phone = phone;
        this.countryCode = countryCode;
    }

    public PersistedUser(String userName,
                         String password,
                         List<String> roles,
                         String organization,
                         String email,
                         int authProviderId,
                         String phone,
                         String countryCode,
                         boolean has2FAEnabled,
                         String firstName,
                         String lastName) {
        super(userName, password, roles, organization, email, authProviderId, phone, countryCode, has2FAEnabled);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersistedUser(String userName,
                         String password,
                         String passwordHash,
                         String salt,
                         List<String> roles,
                         String organization,
                         String email,
                         int authProviderId,
                         String phone,
                         String countryCode,
                         boolean has2FAEnabled,
                         String firstName,
                         String lastName) {
        super(userName, password, roles, organization, email, passwordHash, salt, authProviderId, phone, countryCode, has2FAEnabled, firstName, lastName);
    }

    public PersistedUser(User u) {
        this(u.username,
            u.password,
            u.passwordHash,
            u.salt,
            u.roles,
            u.organization,
            u.email,
            u.authProviderId,
            u.phone,
            u.countryCode,
            u.has2FAEnabled,
            u.firstName,
            u.lastName
        );
    }

    public String getId() {
        return id;
    }

    public User toBase() {
        return new User(this.username,
                this.password,
                this.roles,
                this.organization,
                this.email,
                this.passwordHash,
                this.salt,
                this.authProviderId,
                this.phone,
                this.countryCode,
                this.has2FAEnabled,
                this.firstName,
                this.lastName
        );
    }
}
