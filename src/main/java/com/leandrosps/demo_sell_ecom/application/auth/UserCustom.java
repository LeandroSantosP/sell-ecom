package com.leandrosps.demo_sell_ecom.application.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

public class UserCustom extends User {

	@Getter
	private String userEmail;

	public UserCustom(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	public UserCustom(String username, String password, Collection<? extends GrantedAuthority> authorities, String email) {
		this(username, password, true, true, true, true, authorities);
		this.userEmail = email;
	}
}
