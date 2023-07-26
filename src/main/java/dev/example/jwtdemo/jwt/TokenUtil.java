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

import dev.example.jwtdemo.jwt.jwts.JwtsTokenComposer;

public class TokenUtil {

	public static RSAPrivateKey getPrivateKey(String resourceName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = loadKey(resourceName);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(spec);
		return privateKey;
	}

	public static RSAPublicKey getPublicKey(String resourceName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = loadKey(resourceName);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);
		return publicKey;
	}

	private static byte[] loadKey(String resourceName) throws IOException {
		String key = new String(
				JwtsTokenComposer.class.getClassLoader().getResourceAsStream(resourceName).readAllBytes(),
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
