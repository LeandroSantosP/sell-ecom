package com.leandrosps.demo_sell_ecom.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class MyCouponTest {

    @Test
    void shouldCreateACouponAndCalculateTheDiscount() {
        var coupon = MyCoupon.craetes("SAVE10", 10, true, 2, 0, LocalDate.of(2024, 03, 21));
        var discount = coupon.calcDiscountIn(100);
        coupon.increaseUsage();
        assertEquals(10, discount);
        assertEquals(2, coupon.getQuantity());
        assertEquals(1, coupon.getUsed());
    }

    @Test
    void shouldValideteACouponExpiredDate() {
        var coupon = MyCoupon.craetes("SAVE10", 10, true, 2, 0, LocalDate.of(2024, 03, 21));
        var expiredDate = LocalDate.of(2024, 04, 21);
        var NotExpiredDate = LocalDate.of(2024, 02, 21);
        assertTrue(coupon.isValid(NotExpiredDate));
        assertFalse(coupon.isValid(expiredDate));
    }

}
