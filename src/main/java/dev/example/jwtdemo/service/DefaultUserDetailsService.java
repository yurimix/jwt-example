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

	// TODO: Mock implementation: all users are valid when username == password
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var password = passwordEncoder.encode(username);
		var ub = User.builder().
			username(username).
			password(password).
			authorities(Arrays.asList(username.equals("admin") ? Role.ADMIN_AUTHORITY: Role.USER_AUTHORITY));
		if (username.equals("expired")) {
			ub.accountExpired(true);
		}
		if (username.equals("locked")) {
			ub.accountLocked(true);
		}
		return ub.build();
	}
}
