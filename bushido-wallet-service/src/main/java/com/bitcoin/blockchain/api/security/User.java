package com.bitcoin.blockchain.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jesion on 2014-12-28.
 */
public class User implements UserDetails {

	private Collection<? extends GrantedAuthority> roles;
	private String username;
	private String password;

	public User() {
		roles = new ArrayList<GrantedAuthority>();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}

	public void addAuthority(GrantedAuthority role) {
		if (roles == null) {
			roles = new ArrayList<GrantedAuthority>();
		}
		((List) roles).add(role);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
