package dev.example.jwtdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.example.jwtdemo.model.Role;
import jakarta.annotation.security.RolesAllowed;

@RestController
public class HelloWorldController {

	// allowed for USER and ADMIN
	@RequestMapping({ "/hello/user" })
	@RolesAllowed({Role.USER_ROLE})
	public String helloUser() {
		return "Hello USER!";
	}

	// allowed for ADMIN only
	@RequestMapping({ "/hello/admin" })
	@RolesAllowed({Role.ADMIN_ROLE})
	public String helloAdmin() {
		return "Hello ADMIN!";
	}
}
