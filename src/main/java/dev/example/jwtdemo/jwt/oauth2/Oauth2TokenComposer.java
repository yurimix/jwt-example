package dev.example.jwtdemo.jwt.oauth2;

import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import dev.example.jwtdemo.jwt.JwtTokenComposer;

@Component
@ConditionalOnBean(Oauth2Config.class)
public class Oauth2TokenComposer implements JwtTokenComposer {

	@Value("${jwt.token.validity:3600}")
	private long jwtTokenValidity;

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private JwtDecoder jwtDecoder;

	private static final Logger logger = LoggerFactory.getLogger(Oauth2TokenComposer.class);

	public Oauth2TokenComposer() {
		logger.info("OAUTH2 token composer instantiated");
	}

	@Override
	public String getUsernameFromToken(String token) {
		return jwtDecoder.decode(token).getSubject();
	}

	@Override
	public String generateToken(String subject) {
		var now = Instant.now();
		var claims = JwtClaimsSet.builder().
			subject(subject).
			issuer(ISSUER).
			issuedAt(now).
			expiresAt(now.plusSeconds(jwtTokenValidity)).
			build();
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

	@Override
	public Boolean validateToken(String token, String subject) {
		var jwt = jwtDecoder.decode(token);
		return (subject.equals(jwt.getSubject()) && !isTokenExpired(jwt));
	}

	private Boolean isTokenExpired(Jwt token) {
		final Date expiration = Date.from(token.getExpiresAt());
		return expiration.before(new Date());
	}

}
