package com.leandrosps.demo_sell_ecom.application.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.geteways.MyClock;

@Service
public class CouponService {
    private CouponRepository couponRepository;
    private MyClock clock;

    public CouponService(CouponRepository couponRepository, MyClock clock) {
        this.couponRepository = couponRepository;
        this.clock = clock;
    }

    public record CreateCouponInput(String code, int percentage, int quantity, LocalDate expiredAt) {
    }

    public void create(CreateCouponInput input) {
        var coupon = MyCoupon.create(input.code(), input.percentage(), input.quantity(), input.expiredAt(), this.clock.getCurrentDate().toLocalDate());
        var couponData = this.couponRepository.findById(coupon.getCode());
        if (couponData.isPresent()) {
            throw new RuntimeException("This coupon already exists!");
        }
        this.couponRepository.persiste(coupon);
    }

    public record GetCouponOutput(String code, Integer percentage, Boolean isAvealible, Integer quantity, Integer used,
            LocalDate expiredAt, LocalDate createdAt) {
    }

    public GetCouponOutput getCoupon(String code) {
        var coupon = this.couponRepository.getByCode(code);
        return new GetCouponOutput(coupon.getCode(), coupon.getPercentage(), coupon.isAvailable(), coupon.getQuantity(),
                coupon.getUsed(), coupon.getExpiredAt(), coupon.getCreatedAt());
    }
}
