package com.leandrosps.demo_sell_ecom.db.dbmodels;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("clients") 
public record ClientDbModel(@Id String id, String name, String email, String city, LocalDate birthday,
        LocalDateTime create_at) {
}