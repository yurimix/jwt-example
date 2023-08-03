package dev.example.jwtdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.example.jwtdemo.jwt.JwtTokenComposer;
import io.swagger.v3.oas.annotations.media.Schema;

record AuthenticationRequest(
		@Schema(name = "username", example = "user", description = "User name")
		@JsonProperty(required = true, value = "username") String username,
		@Schema(name = "password", example = "user", description = "User password, for this demo must be equal to the user name")
		@JsonProperty(required = true, value = "password") String password) {

		@JsonCreator
		public AuthenticationRequest(String username, String password) {
			this.username = username;
			this.password = password;
		}
	}

record AuthenticationResponse(String token) {}

@CrossOrigin
@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenComposer tokenComposer;

	@Autowired
	private UserDetailsService userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.username(), authenticationRequest.password());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
		return ResponseEntity.ok(new AuthenticationResponse(tokenComposer.generateToken(userDetails.getUsername())));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (Exception ex) {
			logger.error(String.format("Could not authenticate user `%s`: %s", username, ex.getMessage()));
			throw ex;
		}
	}
}
