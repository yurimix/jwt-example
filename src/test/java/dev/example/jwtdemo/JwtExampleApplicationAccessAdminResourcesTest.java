package dev.example.jwtdemo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class JwtExampleApplicationAccessAdminResourcesTest extends JwtExampleApplicationTest {

	@LocalServerPort
	private int port;

	private String token;

	@BeforeAll
	public void init() {
		this.token = authenticate("admin", "admin");
	}
	
	@Test
	public void testUserResourceAccess() {
		HttpEntity<String> entity = new HttpEntity<>(getHeaders(this.token));
		ResponseEntity<String> response = restTemplate.exchange(getUrl("/hello/user"),
				HttpMethod.GET, entity, String.class);
		assertThat(response.getBody()).isEqualTo("Hello USER!");
	}

	@Test
	public void testAdminResourceAccess() {
		HttpEntity<String> entity = new HttpEntity<>(getHeaders(this.token));
		ResponseEntity<String> response = restTemplate.exchange(getUrl("/hello/admin"),
				HttpMethod.GET, entity, String.class);
		assertThat(response.getBody()).isEqualTo("Hello ADMIN!");
	}

}
