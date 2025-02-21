package com.leandrosps.demo_sell_ecom.db.dbmodels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Table("users")
public class UserDbModel implements UserDetails {
	@Id
	private Integer id;
	private String username;
	private String password;
	private String email;
	private boolean enabled;

	@Column(value = "verification_code")
	private String validationCode;

	@Column(value = "verification_expiration_at")
	private LocalDateTime verificationCodeExpiresAt;

	@Column(value = "roles")
	private String roles;

	public UserDbModel(String username, String password, String email) {
		this.id = null;
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = false;
		this.validationCode = this.generateVerificationCode();
		this.verificationCodeExpiresAt = LocalDateTime.now().plusMinutes(15);
		this.roles = "user";
	}

	public UserDbModel(){
    }

	private String generateVerificationCode() {
		Random random = new Random();
		int code = random.nextInt(900000) + 100000;
		return String.valueOf(code);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		var roles_json = new JSONArray(this.getRoles());
		List<String> roles = new ArrayList<>();
		for (int i = 0; i < roles_json.length(); i++) {
			roles.add(roles_json.getString(i));
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		if (roles != null) {
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
		}
		return authorities;
	}

}
