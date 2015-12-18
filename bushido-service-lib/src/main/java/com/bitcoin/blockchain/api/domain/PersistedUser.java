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
                         String pinHash,
                         List<String> roles,
                         String organization,
                         String email) {
        super(userName, pinHash, roles, organization, email);
    }

    public PersistedUser(String userName,
                         String pinHash,
                         List<String> roles,
                         String organization,
                         String email,
                         String phone,
                         String countryCode) {
        super(userName, pinHash, roles, organization, email);
        this.phone = phone;
        this.countryCode = countryCode;
    }

    public PersistedUser(String userName,
                         String pinHash,
                         List<String> roles,
                         String organization,
                         String email,
                         int authProviderId,
                         String phone,
                         String countryCode,
                         boolean has2FAEnabled,
                         String firstName,
                         String lastName) {
        super(userName, pinHash, roles, organization, email, authProviderId, phone, countryCode, has2FAEnabled);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PersistedUser(String userName,
                         String pinHash,
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
        super(userName,
                pinHash,
                roles,
                organization,
                email,
                passwordHash,
                salt,
                authProviderId,
                phone,
                countryCode,
                has2FAEnabled,
                firstName,
                lastName
        );
    }

    public PersistedUser(User u) {
        this(u.username,
            u.pinHash,
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
                this.pinHash,
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

    public String toString() {
        return "Firstname: " + firstName + " Lastname " + lastName + "Username: " + username + " Organization: " + organization + " Email: " + email + " Phone code: " + countryCode + " Telephone: " + phone;
    }
}
