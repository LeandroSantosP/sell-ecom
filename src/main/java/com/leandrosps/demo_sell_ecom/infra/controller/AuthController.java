package com.leandrosps.demo_sell_ecom.infra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.auth.AuthService;
import com.leandrosps.demo_sell_ecom.application.auth.AuthService.AuthenticateOutput;
import com.leandrosps.demo_sell_ecom.application.auth.AuthService.RegisterUserDto;
import com.leandrosps.demo_sell_ecom.application.auth.AuthService.VerifyCodeInput;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
		System.out.println("TEST1: "+authService);
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

	@PostMapping("/verify")
	public ResponseEntity<Integer> verify(@Valid @RequestBody VerifyCodeInput input) {
		var output = this.authService.verify(input);
		return ResponseEntity.ok(output);
	}

	@PatchMapping("/resend")
	public ResponseEntity<Void> resend(@RequestParam String email) {
		this.authService.resend(email);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('admin')")
	@PatchMapping("/priv/bestow-role/{user_id}")
	public ResponseEntity<Void> bestowTole(@PathVariable Integer user_id, @RequestBody List<String> roles) {
		this.authService.bestowRole(roles, user_id);
		return ResponseEntity.ok().build();
	}
}
