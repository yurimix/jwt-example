package dev.example.jwtdemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.JsonPath;

public class JwtExampleApplicationAuthenticationTest extends JwtExampleApplicationTest {


	private HttpHeaders headers;

	@BeforeAll
	public void init() {
		headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
	}

	@Test
	public void testAuthenticate() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<>("""
			{
				"username": "test_user",
				"password": "test_user"
			}""", headers);

		ResponseEntity<String> response = restTemplate.exchange(getUrl(), HttpMethod.POST, entity, String.class);

		assertTrue(response.getStatusCode().is2xxSuccessful());
		var dc = JsonPath.parse(response.getBody());
		String token = dc.read("$['token']");
		assertThat(token).isNotNull();
	}

	@Test
	public void testAuthenticateBadPassword() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<>("""
				{
				  "username": "test_user",
				  "password": "user_test"
				}
				""", headers);

		ResponseEntity<String> response = restTemplate.exchange(getUrl(), HttpMethod.POST, entity, String.class);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

	private String getUrl() {
		return getUrl("/authenticate");
	}

}
