package com.leandrosps.demo_sell_ecom.db.dbmodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("products")
public record ProductDbModel(@Id String id, String name, Long price, Integer stoke, String image_ref, String brand_id) {
}