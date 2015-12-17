package com.bitcoin.blockchain.api.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jesion on 2014-12-28.
 */

public class User implements Serializable {

    public List<String> roles;

    public String username;

    public String pinHash;

    public String passwordHash;

    public String salt;

    public String organization;

    public String email;

    public int authProviderId;

    public String phone;

    public String countryCode;

    public boolean has2FAEnabled;

    public String firstName;

    public String lastName;

    public User() {

    }

    public User(String userName, String pinHash, List<String> roles, String organization) {
        this(userName, pinHash);
        this.roles = roles;
        this.organization = organization;
    }

    public User(String userName,
                String pinHash,
                List<String> roles,
                String organization,
                String email) {
        this(userName, pinHash);
        this.roles = roles;
        this.organization = organization;
        this.email = email;
    }

    public User(String userName,
                String pinHash,
                List<String> roles,
                String organization,
                String email,
                int authProviderId,
                String phone,
                String countryCode,
                boolean has2FAEnabled) {
        this(userName, pinHash);
        this.roles = roles;
        this.organization = organization;
        this.email = email;
        this.authProviderId = authProviderId;
        this.phone = phone;
        this.countryCode = countryCode;
        this.has2FAEnabled = has2FAEnabled;
    }

    public User(String userName,
                String pinHash,
                List<String> roles,
                String organization,
                String email,
                String passwordHash,
                String salt,
                int authProviderId,
                String phone,
                String countryCode,
                boolean has2FAEnabled,
                String firstName,
                String lastName) {
        this(userName, pinHash, roles, organization, email, authProviderId, phone, countryCode, has2FAEnabled);
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String userName,
                String pinHash,
                List<String> roles,
                String organization,
                String passwordHash,
                String salt,
                int authProviderId,
                String phone,
                String countryCode,
                boolean has2FAEnabled) {
        this(userName, pinHash, roles, organization);
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.authProviderId = authProviderId;
        this.phone = phone;
        this.countryCode = countryCode;
        this.has2FAEnabled = has2FAEnabled;
    }

    public User(String username, String pinHash) {
        this.username = username;
        this.pinHash = pinHash;
    }
}
