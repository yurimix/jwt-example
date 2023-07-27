package dev.example.jwtdemo.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.example.jwtdemo.model.Role;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;

	enum PredefinedUsers {
		ADMIN_USER   ("admin"),
		EXPIRED_USER ("expired"),
		LOCKED_USER  ("locked");

		private final String userName;
		private PredefinedUsers(String userName) {
			this.userName = userName;
		}
	};

	// TODO: Mock implementation: all users are valid when username == password
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var password = passwordEncoder.encode(username);
		var ub = User.builder().
			username(username).
			password(password).
			authorities(
				Arrays.asList(
					username.equals(PredefinedUsers.ADMIN_USER.userName) ? 
						Role.ADMIN_AUTHORITY: Role.USER_AUTHORITY)
			);
		if (username.equals(PredefinedUsers.EXPIRED_USER.userName)) {
			ub.accountExpired(true);
		}
		if (username.equals(PredefinedUsers.LOCKED_USER.userName)) {
			ub.accountLocked(true);
		}
		return ub.build();
	}
}
