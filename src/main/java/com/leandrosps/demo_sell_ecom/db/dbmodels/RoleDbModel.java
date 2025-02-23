package com.leandrosps.demo_sell_ecom.db.dbmodels;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("roles")
public record RoleDbModel(@Id Integer id, String name) {
}