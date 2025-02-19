package com.leandrosps.demo_sell_ecom.db.dbmodels;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Table("users")
public class UserDbModel {
	@Id
	private Integer id;
	private String username;
	private String password;
	private String email;

	@Column(value = "roles")
	private List<String> roles;
}
