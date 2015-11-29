package com.bitcoin.blockchain.api.security;

import com.bitcoin.blockchain.api.domain.User;
import com.bitcoin.blockchain.api.persistence.UserDAO;
import com.bitcoin.blockchain.api.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Jesion on 2014-12-28.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	public UserDAO userDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userDAO.get(username);

		if (user != null) {
			return UserUtil.toUser(user);
		}

		throw new UsernameNotFoundException("User " + username + " is not authenticated to access this application.");
	}
}
