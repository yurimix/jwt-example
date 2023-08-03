package dev.example.jwtdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.example.jwtdemo.model.Role;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

	// allowed for USER and ADMIN
	@GetMapping({ "/user" })
	@RolesAllowed({Role.USER_ROLE})
	public String helloUser() {
		return "Hello USER!";
	}

	// allowed for ADMIN only
	@GetMapping({ "/admin" })
	@RolesAllowed({Role.ADMIN_ROLE})
	public String helloAdmin() {
		return "Hello ADMIN!";
	}
}
