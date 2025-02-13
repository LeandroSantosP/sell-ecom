package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyCoupon {
    private String code;
    private int percentage;
    private boolean isAvailable;
    private int usageLimiit;
    private int usageLimiitCurrent;
    private LocalDate expiredAt;
    private LocalDate createdAt;

    static public MyCoupon craetes(String code, int percentage, boolean isAvailable, int usageLimiit,int usageLimiitCurrent,
            LocalDate expiredAt) {
        return new MyCoupon(code, percentage, isAvailable, usageLimiit, usageLimiitCurrent, expiredAt, LocalDate.now());
    }

    public double calcDiscountIn(double value) {
        return (value * this.percentage) / 100;
    }

     public void increaseUsage() {
        this.usageLimiitCurrent++;
    }

    public boolean isValid(LocalDate date) {
        if (date.isAfter(this.expiredAt)) {
            return false;
        }
        return true;
    }
}

