package dev.example.jwtdemo.jwt.jwts;

import static dev.example.jwtdemo.jwt.TokenUtil.getPrivateKey;
import static dev.example.jwtdemo.jwt.TokenUtil.getPublicKey;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import dev.example.jwtdemo.jwt.JwtTokenComposer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
@ConditionalOnProperty(value="jwt.token.composer", havingValue = "jwts")
public class JwtsTokenComposer implements JwtTokenComposer {

	@Value("${pem.public.key}")
	private String pemPublicKey;

	@Value("${pem.private.key}")
	private String pemPrivateKey;

	@Value("${jwt.token.validity:3600}")
	private long jwtTokenValidity;
	
	private RSAPrivateKey rsaPrivateKey;
	private RSAPublicKey rsaPublicKey;

	private static final Logger logger = LoggerFactory.getLogger(JwtsTokenComposer.class);

	public JwtsTokenComposer() {
		logger.info("JWTS token composer instantiated");
	}
	
	@PostConstruct
	public void init() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		this.rsaPrivateKey = getPrivateKey(pemPrivateKey);
		this.rsaPublicKey = getPublicKey(pemPublicKey);
	}

	@Override
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	@Override
	public String generateToken(String subject) {
		var now = Instant.now();
		return Jwts.builder().
			setSubject(subject).
			setIssuer(ISSUER).
			setIssuedAt(Date.from(now)).
			setExpiration(Date.from(now.plusSeconds(jwtTokenValidity))).
			signWith(SignatureAlgorithm.RS256, rsaPrivateKey).compact();
	}

	@Override
	public Boolean validateToken(String token, String subject) {
		final String username = getUsernameFromToken(subject);
		return (username.equals(subject) && !isTokenExpired(token));
	}

	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(rsaPublicKey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

}
