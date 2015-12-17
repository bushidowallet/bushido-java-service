package com.bitcoin.blockchain.api.domain;

import java.util.List;

/**
 * Created by Jesion on 2015-06-02.
 */
public class RegUser extends User {

    public String regCode;

    public String password;

    public RegUser(String userName,
                   String password,
                   List<String> roles,
                   String organization,
                   String email,
                   String regCode,
                   String phone,
                   String countryCode,
                   String firstName,
                   String lastName) {
        super(userName, null, roles, organization, email);
        this.password = password;
        this.regCode = regCode;
        this.phone = phone;
        this.countryCode = countryCode;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public RegUser() {

    }
}
