package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDate;

import com.leandrosps.demo_sell_ecom.infra.errors.CouponUsageLimitEx;
import com.leandrosps.demo_sell_ecom.infra.errors.InvalidCouponExpireDate;

import lombok.Getter;

@Getter
public class MyCoupon {
    private String code;
    private int percentage;
    private boolean isAvailable;
    private int quantity;
    private int used;
    private LocalDate expiredAt;
    private LocalDate createdAt;

    public MyCoupon(String code, int percentage, boolean isAvailable, int quantity, int used, LocalDate expiredAt,
            LocalDate createdAt) {

        if (code == null || code.isBlank()) {
            this.code = couponCodeValidation("SAVE".concat(Integer.toString(percentage)));
        } else {
            this.code = couponCodeValidation(code);
        }


        if (expiredAt.isBefore(createdAt)) {
            throw new InvalidCouponExpireDate();
        }

        this.percentage = percentage;
        this.isAvailable = isAvailable;
        this.quantity = quantity;
        this.used = used;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    private String couponCodeValidation(String code) {
        var codeSlited = String.copyValueOf(code.toCharArray()).split("SAVE");
        if (!code.startsWith("SAVE") || codeSlited.length != 2 || !codeSlited[0].equals("")
                || !codeSlited[1].matches("\\d+$")) {
            throw new RuntimeException(
                    "Invalid Coupon Code, The coupon must starts with SAVE follow by the percentage discount!");
        }
        return code;
    }

    static public MyCoupon create(String code, int percentage, int quantity, LocalDate expiredAt, LocalDate createdAt) {
        var isAvailable = false;
        if (quantity > 0) {
            isAvailable = true;
        }
        return new MyCoupon(code, percentage, isAvailable, quantity, 0, expiredAt, createdAt);
    }

    public double calcDiscountIn(double value) {
        this.increaseUsage();
        return (value * this.percentage) / 100;
    }

    public void decreseUsage() {

        if (this.used - 1 < 0) {
            throw new CouponUsageLimitEx(this.code);
        }

        if (this.used - 1 != this.quantity) {
            this.isAvailable = true;
        }

        this.used--;
    }

    private void increaseUsage() {
        if (this.used + 1 > this.quantity) {
            throw new CouponUsageLimitEx(this.code);
        }
        if (this.used + 1 == this.quantity) {
            this.isAvailable = false;
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
