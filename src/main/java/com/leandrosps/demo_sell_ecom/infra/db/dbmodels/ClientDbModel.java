package com.leandrosps.demo_sell_ecom.infra.db.dbmodels;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("clients")
public record ClientDbModel(@Id String id, String name,
        @Column("fk_email") String fkEmail, String city, LocalDate birthday, LocalDateTime create_at) {
}