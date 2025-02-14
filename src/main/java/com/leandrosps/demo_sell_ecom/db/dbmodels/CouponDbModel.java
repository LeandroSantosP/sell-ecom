package com.leandrosps.demo_sell_ecom.db.dbmodels;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Table("coupons")
public class CouponDbModel {
    @Id
    String code;
    int percentage;
    int usage_limit;
    int used;
    boolean is_available;
    LocalDate expired_at;
    LocalDate created_at;
}
