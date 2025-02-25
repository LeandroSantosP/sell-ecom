package com.leandrosps.demo_sell_ecom.infra.db;

import org.springframework.data.repository.ListCrudRepository;

import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.ProductDbModel;

public interface ProductRepository extends ListCrudRepository<ProductDbModel, String> {
}