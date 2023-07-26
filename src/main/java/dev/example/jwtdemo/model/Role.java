package dev.example.jwtdemo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Role {

	public static final String ADMIN_ROLE = "ADMIN";
	public static final String USER_ROLE = "USER";
	
	// name of authority must stared with "ROLE_" because it is a default prefix used by Jsr250AuthorizationManager
	public static final GrantedAuthority[] USER_AUTHORITY = {new SimpleGrantedAuthority("ROLE_" + USER_ROLE)};
	public static final GrantedAuthority[] ADMIN_AUTHORITY = {new SimpleGrantedAuthority("ROLE_" + ADMIN_ROLE), USER_AUTHORITY[0]};
}
