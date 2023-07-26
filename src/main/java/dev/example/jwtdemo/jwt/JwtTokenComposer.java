package dev.example.jwtdemo.jwt;

public interface JwtTokenComposer {

	static final String ISSUER = "dev.example.yurimix";
	
	String getUsernameFromToken(String token);

	String generateToken(String subject);

	Boolean validateToken(String token, String subject);
}
