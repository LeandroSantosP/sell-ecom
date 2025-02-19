package com.leandrosps.demo_sell_ecom.application;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private CustomUserDetailsService customUserDetailsService;

	public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String rawPassword = authentication.getCredentials().toString();
		UserDetails userdetails = this.customUserDetailsService.loadUserByUsername(username);
		var encoder = new BCryptPasswordEncoder();
		if (encoder.matches(rawPassword, userdetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(userdetails, userdetails.getUsername(),
					userdetails.getAuthorities());
		} else {
			throw new BadCredentialsException("Bad Credentials");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}