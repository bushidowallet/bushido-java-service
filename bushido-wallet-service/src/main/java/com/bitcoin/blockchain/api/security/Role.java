package com.bitcoin.blockchain.api.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Jesion on 2014-12-28.
 */
public class Role implements GrantedAuthority {

    private String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
