package com.leandrosps.demo_sell_ecom.db;

import org.springframework.data.repository.ListCrudRepository;

import com.leandrosps.demo_sell_ecom.db.dbmodels.ProductDbModel;

public interface ProductRepository extends ListCrudRepository<ProductDbModel, String> {
}