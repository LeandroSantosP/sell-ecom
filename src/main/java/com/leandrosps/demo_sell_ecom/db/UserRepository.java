package com.leandrosps.demo_sell_ecom.db;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import com.leandrosps.demo_sell_ecom.db.dbmodels.UserDbModel;

@Repository
public interface UserRepository extends ListCrudRepository<UserDbModel, Integer> {
	public Optional<UserDbModel> findByEmail(String email);
	public Optional<UserDbModel> findByUsername(String username);
}
