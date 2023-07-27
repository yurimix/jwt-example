package dev.example.jwtdemo.jwt.oauth2;

import static dev.example.jwtdemo.jwt.TokenUtil.getPrivateKey;
import static dev.example.jwtdemo.jwt.TokenUtil.getPublicKey;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
@ConditionalOnProperty(value="jwt.token.composer", havingValue = "oauth2")
public class Oauth2Config {

	@Value("${pem.public.key}")
	private String pemPublicKey;

	@Value("${pem.private.key}")
	private String pemPrivateKey;

	@Bean
	public JwtEncoder jwtEncoder() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		var rsaPublicKey = getPublicKey(pemPublicKey);
		var rsaPrivateKey = getPrivateKey(pemPrivateKey);

		var jwk = new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey).build();
		var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	@Bean
	public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return NimbusJwtDecoder.withPublicKey(getPublicKey(pemPublicKey)).build();
	}

}