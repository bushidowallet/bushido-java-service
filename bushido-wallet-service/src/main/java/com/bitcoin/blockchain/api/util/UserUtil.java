package com.bitcoin.blockchain.api.util;

import com.bitcoin.blockchain.api.domain.User;
import com.bitcoin.blockchain.api.security.Role;

/**
 * Created by Jesion on 2015-02-25.
 */
public class UserUtil {

    public static com.bitcoin.blockchain.api.security.User toUser(User u) {
        com.bitcoin.blockchain.api.security.User user = new com.bitcoin.blockchain.api.security.User();
        user.setUsername(u.username);
        user.setPassword(u.passwordHash);
        if (u.roles != null) {
            for (String role : u.roles) {
                user.addAuthority(new Role(role));
            }
        }
        return user;
    }
}
