package dev.example.jwtdemo;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.JsonPath;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(classes = JwtExampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class JwtExampleApplicationTest {

	@LocalServerPort
	protected int port;

	protected TestRestTemplate restTemplate = new TestRestTemplate();;

	protected HttpHeaders getHeaders(String token) {
		var headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));
		return headers;
	}

	protected String authenticate(String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
		HttpEntity<String> entity = new HttpEntity<>(
			String.format("""
				{
				  "username": "%s",
				  "password": "%s"
				}
				""", username, password), headers);
		
		ResponseEntity<String> response = restTemplate.exchange(getUrl("/authenticate"),
				HttpMethod.POST, entity, String.class);
		return JsonPath.parse(response.getBody()).read("$['token']");
	}

	protected String getUrl(String path) {
		return "http://localhost:" + port + path;
	}

}
