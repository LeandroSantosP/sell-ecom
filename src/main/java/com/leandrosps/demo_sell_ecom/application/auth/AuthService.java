package com.leandrosps.demo_sell_ecom.application.auth;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import com.leandrosps.demo_sell_ecom.db.UserRepository;
import com.leandrosps.demo_sell_ecom.db.dbmodels.UserDbModel;

@Service
public class AuthService {

	private UserRepository userRepository;
	private AuthenticationManager authenticationManager;
	private JwtEncoder jwtEncoder;
	private PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager,
			JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authenticationManager = authenticationManager;
		this.jwtEncoder = jwtEncoder;
		this.passwordEncoder = passwordEncoder;
	}

	public record RegisterUserDto(String email, String password, String username) {
	}

	public void signUp(RegisterUserDto input) {
		var userOp = this.userRepository.findByEmail(input.email());
		System.out.println("userOp"+input);

		if (userOp.isPresent()) {
			new BadCredentialsException("Ivalid Credentails!");
		}
		/* TODO:  send the email with the code  */
		
		var user = new UserDbModel(input.username(), passwordEncoder.encode(input.password()), input.email());

		//this.userRepository.save(user);
	}

	public record AuthenticateOutput(String token, Instant expiredIn) {
	}

	public AuthenticateOutput authenticate(String username, String password) {
		var userOp = this.userRepository.findByUsername(username);

		if (userOp.isEmpty()) {
			throw new BadCredentialsException("Invalid password or username!");
		}
		var user = userOp.get();

		if (!user.isEnabled()) {
			throw new RuntimeException("Account not verified, Please verify your account!");
		}

		//this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		var token = this.generateJwtToken(user);
		return new AuthenticateOutput(token.getTokenValue(), token.getExpiresAt());
	}

	/* Maybe create an service fot this? */
	private Jwt generateJwtToken(UserDbModel user) {
		var expiredIn = Instant.now().plusSeconds(300L);

		List<String> authorities = new ArrayList<>();

		if (user.getAuthorities() instanceof Collection) {
			Collection<?> authoritiesCollection = user.getAuthorities();
			for (Object authority : authoritiesCollection) {
				authorities.add(authority.toString());
			}
		}

		var claims = JwtClaimsSet.builder().claim("user_roles", authorities).issuer("sell-ecom-backend")
				.subject(user.getId().toString()).expiresAt(expiredIn).build();

		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claims);
		return this.jwtEncoder.encode(jwtEncoderParameters);
	}
}
