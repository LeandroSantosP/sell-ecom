package com.leandrosps.demo_sell_ecom.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.auth.AuthService;
import com.leandrosps.demo_sell_ecom.application.auth.AuthService.AuthenticateOutput;
import com.leandrosps.demo_sell_ecom.application.auth.AuthService.RegisterUserDto;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	public record SignInInput(String username, String password) {
		
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthenticateOutput> signin(@Valid @RequestBody SignInInput input) {
		var output = this.authService.authenticate(input.username(), input.password());
		return ResponseEntity.ok(output);
	}

	@PostMapping("/signup")
	public ResponseEntity<Void> signUp(@Valid @RequestBody RegisterUserDto input) {
		this.authService.signUp(input);
		return ResponseEntity.ok().build();
	}
	
}
