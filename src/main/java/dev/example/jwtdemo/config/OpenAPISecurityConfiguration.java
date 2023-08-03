package dev.example.jwtdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition
public class OpenAPISecurityConfiguration {

	@Bean
	public OpenAPI customizeOpenAPI() {
		final String securitySchemeName = "BearerAuth";
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
			.components(
				new Components().addSecuritySchemes(
					securitySchemeName, new SecurityScheme()
						.name(securitySchemeName)
						.type(SecurityScheme.Type.HTTP)
						.scheme("Bearer")
						.description("""
							Provide the JWT token.
							JWT token can be obtained from the authentication API.<br>
							For testing use the credentials <strong>user/user</strong> or
							<strong>admin/admin</strong>.""")
						.bearerFormat("JWT")
					)
			);

	}
}
