package com.leandrosps.demo_sell_ecom.application.auth;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.leandrosps.demo_sell_ecom.db.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var userDatabase = this.userRepository.findByUsername(username);
		if (userDatabase.isEmpty()) {
			throw new BadCredentialsException("Bad Credentials");
		}
		var user = userDatabase.get();
		var roles_json = new JSONArray(user.getRoles());
		List<String> roles = new ArrayList<>();
		for (int i = 0; i < roles_json.length(); i++) {
			roles.add(roles_json.getString(i));
		}
		return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

}