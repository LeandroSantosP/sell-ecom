package com.leandrosps.demo_sell_ecom.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;

import java.time.LocalDate;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.leandrosps.demo_sell_ecom.errors.CouponUsageLimitEx;

public class MyCouponTest {

    @Test
    void shouldCreateACouponAndCalculateTheDiscount() {
        var coupon = MyCoupon.craetes("SAVE10", 10, true, 2, 0, LocalDate.of(2024, 03, 21));
        var discount = coupon.calcDiscountIn(100);

        assertEquals(10, discount);
        assertEquals(2, coupon.getQuantity());
        assertEquals(1, coupon.getUsed());
        assertEquals(true, coupon.isAvailable());
    }

    @Test
    @Tag("ere")
    void shouldNotBeAbleIncreaseUsageIfReachItsLimit() {
        var coupon = MyCoupon.craetes("SAVE10", 10, true, 2, 0, LocalDate.of(2024, 03, 21));
        coupon.calcDiscountIn(100);
        coupon.calcDiscountIn(100);
        assertThrows("This coupon active its usage limite: SAVE10", CouponUsageLimitEx.class, () -> coupon.calcDiscountIn(100));
    }

    @Test
    void shouldChangeTheStatusOnceReachItsUsageLimit() {
        var coupon = MyCoupon.craetes("SAVE10", 10, true, 2, 0, LocalDate.of(2024, 03, 21));
        coupon.calcDiscountIn(100);
        coupon.calcDiscountIn(100);

        assertEquals(coupon.getUsed(), 2);
        assertEquals(coupon.isAvailable(), false);
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
