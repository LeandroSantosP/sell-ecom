package com.leandrosps.demo_sell_ecom.db;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("orders")
public record OrderDbModel(
                @Id String id,
                @Column("total") Long total,
                @Column("status") String status,
                @Column("client_id") String client_id,
                @Column("client_email") String client_email,
                @Column("created_at") LocalDateTime created_at) {
}