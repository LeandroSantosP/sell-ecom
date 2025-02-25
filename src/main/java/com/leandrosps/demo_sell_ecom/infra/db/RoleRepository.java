package com.leandrosps.demo_sell_ecom.infra.db;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.RoleDbModel;

@Repository
public interface RoleRepository extends ListCrudRepository<RoleDbModel, Integer> {
	public Optional<RoleDbModel> findByName(String name);
}