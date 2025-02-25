package com.leandrosps.demo_sell_ecom.application.auth;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.application.services.ClientService;
import com.leandrosps.demo_sell_ecom.application.services.ClientService.CreateInput;
import com.leandrosps.demo_sell_ecom.infra.db.RoleRepository;
import com.leandrosps.demo_sell_ecom.infra.db.UserRepository;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.UserDbModel;
import com.leandrosps.demo_sell_ecom.infra.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.infra.geteways.Mail;
import com.leandrosps.demo_sell_ecom.infra.geteways.MyClock;

@Service
public class AuthService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private AuthenticationManager authenticationManager;
	private JwtEncoder jwtEncoder;
	private PasswordEncoder passwordEncoder;
	private Mail mail;
	private ClientService clientService;
	private MyClock clock;

	public AuthService(UserRepository userRepository, RoleRepository roleRepository,
			AuthenticationManager authenticationManager, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder,
			Mail mail, ClientService clientService, MyClock clock) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.authenticationManager = authenticationManager;
		this.jwtEncoder = jwtEncoder;
		this.passwordEncoder = passwordEncoder;
		this.mail = mail;
		this.clientService = clientService;
		this.clock = clock;
	}

	public record RegisterUserDto(String email, String password, String username, String name, String city, LocalDate birthday) {
	}

	private void sendMail(String code, String subject, String to) {
		String htmlMessage = "<html>" + "<body style=\"font-family: Arial, sans-serif;\">"
				+ "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
				+ "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
				+ "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
				+ "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
				+ "<h3 style=\"color: #333;\">Verification Code:</h3>"
				+ "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + code + "</p>" + "</div>"
				+ "</div>" + "</body>" + "</html>";
		this.mail.send(to, subject, htmlMessage);
	}

	public void signUp(RegisterUserDto input) {
		var userOp = this.userRepository.findByEmail(input.email());
		var userOp2 = this.userRepository.findByUsername(input.username());

		if (userOp.isPresent() || userOp2.isPresent()) {
			throw new BadCredentialsException("Ivalid Credentails!");
		}
		var user = new UserDbModel(input.username(), passwordEncoder.encode(input.password()), input.email());
		this.sendMail(user.getValidationCode(), "Account Verification", user.getEmail());
		this.userRepository.save(user);
		this.clientService.create(new CreateInput(input.name(), user.getEmail(), user.getPassword(), input.city(), input.birthday()));
	}

	public record VerifyCodeInput(String email, String code) {
	}

	public Integer verify(VerifyCodeInput input) {
		var user = this.userRepository.findByEmail(input.email()).orElseThrow(NotFoundEx::new);

		if (user.isEnabled()) {
			throw new IllegalArgumentException("This user has already been verifield!");
		}

		if (!user.getValidationCode().equals(input.code())) {
			throw new IllegalArgumentException("Invalid verification code!");
		}
		if (user.getVerificationCodeExpiresAt().isBefore(this.clock.getCurrentDate())) {
			throw new IllegalArgumentException("Verificaition code has expired!");
		}

		user.setEnabled(true);
		
		var user_id = this.userRepository.save(user).getId();
		return user_id; 
	}

	public record ResendInput(String email) {
	}

	public void resend(String email) {
		var user = this.userRepository.findByEmail(email).orElseThrow(NotFoundEx::new);
		if (user.isEnabled()) {
			throw new IllegalArgumentException("This user has already been verifield!");
		}
		user.resetValidationCode();
		user.setVerificationCodeExpiresAt(this.clock.getCurrentDate().plusMinutes(15));
		this.sendMail(user.getValidationCode(), "Account Verification", user.getEmail());
		this.userRepository.save(user);
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
		user.isEnabled();

		Map<String, Object> claims = new HashMap<>();
		claims.put("user_roles", authorities);
		claims.put("is_unabled", user.isEnabled());

		var jwtClaimsSet = JwtClaimsSet.builder().claims(c -> c.putAll(claims)).issuer("sell-ecom-backend")
				.subject(user.getId().toString()).expiresAt(expiredIn).build();

		JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
		return this.jwtEncoder.encode(jwtEncoderParameters);
	}

	public void bestowRole(List<String> roles, Integer user_id) {
		var user = this.userRepository.findById(user_id).orElseThrow(NotFoundEx::new);
		this.roleRepository.findById(user_id);
		for (String role : roles) {
			var roleOp = this.roleRepository.findByName(role);
			if (roleOp.isEmpty()) {
				throw new RuntimeException("Invalid Role: " + role);
			}
			user.addRoles(role);
		}
		this.userRepository.save(user);
	}
}
