package com.leandrosps.demo_sell_ecom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.dtos.LoginRequest;
import com.leandrosps.demo_sell_ecom.dtos.LoginResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TokenController {
	
	private final JwtEncoder jwtEncoder;

	public TokenController(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	@PostMapping("/api/pub/login")
	public ResponseEntity<LoginResponse> postMethodName(@RequestBody LoginRequest loginRequest) {
		return null;
	}
	

}
