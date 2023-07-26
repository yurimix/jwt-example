package dev.example.jwtdemo.jwt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenComposer {

	@Value("${pem.public.key}")
	private String pemPublicKey;

	@Value("${pem.private.key}")
	private String pemPrivateKey;

	@Value("${jwt.token.validity:3600}")
	private long jwtTokenValidity;
	
	private RSAPrivateKey rsaPrivateKey;
	private RSAPublicKey rsaPublicKey;

	@PostConstruct
	public void init() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		this.rsaPrivateKey = getPrivateKey();
		this.rsaPublicKey = getPublicKey();
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(String subject) {
		return Jwts.builder().
			setSubject(subject).
			setIssuedAt(new Date(System.currentTimeMillis())).
			setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000)).
			signWith(SignatureAlgorithm.RS256, rsaPrivateKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(rsaPublicKey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = loadKey(pemPrivateKey);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);
		return privateKey;
	}

	private RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = loadKey(pemPublicKey);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
		return publicKey;
	}

	static byte[] loadKey(String resourceName) throws IOException {
		String key = new String(
				JwtTokenComposer.class.getClassLoader().getResourceAsStream(resourceName).readAllBytes(),
				Charset.defaultCharset());
		String pemKey = key.
				replace("-----BEGIN PRIVATE KEY-----", "").
				replace("-----BEGIN PUBLIC KEY-----", "").
				replaceAll(System.lineSeparator(), "").
				replace("-----END PRIVATE KEY-----", "").
				replace("-----END PUBLIC KEY-----", "");
		return Base64.getDecoder().decode(pemKey);
	}

}
