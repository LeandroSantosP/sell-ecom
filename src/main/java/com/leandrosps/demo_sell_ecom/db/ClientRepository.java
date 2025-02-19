package com.leandrosps.demo_sell_ecom.db;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import com.leandrosps.demo_sell_ecom.db.dbmodels.ClientDbModel;

public interface ClientRepository extends ListCrudRepository<ClientDbModel, String> {
    
    Optional<ClientDbModel> findByFkEmail(String fk_email);
}
