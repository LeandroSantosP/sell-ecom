package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyCoupon {
    private String code;
    private int percentage;
    private boolean isAvailable;
    private int quantity;
    private int used;
    private LocalDate expiredAt;
    private LocalDate createdAt;

    static public MyCoupon craetes(String code, int percentage, boolean isAvailable, int quantity, int used,
            LocalDate expiredAt) {
        return new MyCoupon(code, percentage, isAvailable, quantity, used, expiredAt, LocalDate.now());
    }

    public double calcDiscountIn(double value) {
        return (value * this.percentage) / 100;
    }

    public void increaseUsage() {
        if (this.used == this.quantity) {
            throw new RuntimeException();
        }

        this.used++;
    }

    public boolean isValid(LocalDate date) {
        if (date.isAfter(this.expiredAt)) {
            return false;
        }
        return true;
    }
}
