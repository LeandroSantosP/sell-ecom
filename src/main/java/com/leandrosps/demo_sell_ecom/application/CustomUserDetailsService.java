package com.leandrosps.demo_sell_ecom.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.leandrosps.demo_sell_ecom.db.UserRepository;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	private Map<String, UserDetails> userdMemoDb = new HashMap<>();

	public CustomUserDetailsService(UserRepository userRepository) {
		var user1 = User.builder().username("user")
				.password("$2y$10$4XZfm0K6Lv31r0Qs6TlvK.erg8uUzRAeWuS9Aw8MP4jL/Tfw1x8aG").roles("USER").build();
		this.userdMemoDb.put("user", user1);
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var userDatabase = this.userRepository.findByUsername(username);
		if (userDatabase.isEmpty()) {
			throw new BadCredentialsException("Bad Credentials");
		}
		var user = userDatabase.get();
		return new User(user.getUsername(), user.getPassword(),
				getAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (roles != null) {
			for (String role : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
			}
		}
		return authorities;
	}
}
