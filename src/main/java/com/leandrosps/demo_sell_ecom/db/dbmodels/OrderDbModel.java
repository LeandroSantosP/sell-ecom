package com.leandrosps.demo_sell_ecom.db.dbmodels;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Table("orders")
public class OrderDbModel {
    @Id String id;
    @Column("total") long total;
    @Column("status") String status;
    @Column("client_id") String client_id;
    @Column("client_email") String client_email;
    @Column("coupon") String coupon;
    @Column("created_at") LocalDateTime created_at;
}