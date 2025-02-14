package com.leandrosps.demo_sell_ecom.application;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;

@Service
public class CouponService {
    private CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public record CreateCouponInput(String code, int percentage, boolean isAvailable, int quantity,
            LocalDate expiredAt) {
    }

    public void cretea(CreateCouponInput input) {
        this.couponRepository.persiste(MyCoupon.craete(input.code(), input.percentage(), input.isAvailable(),input.quantity(), input.expiredAt()));
    }
}
