package com.leandrosps.demo_sell_ecom.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.leandrosps.demo_sell_ecom.errors.CouponUsageLimitEx;
import com.leandrosps.demo_sell_ecom.errors.InvalidCouponExpireDate;
import com.leandrosps.demo_sell_ecom.geteways.FackClock;
import com.leandrosps.demo_sell_ecom.geteways.MyClock;

@Tag("unit")
public class MyCouponTest {

    private MyClock clock = new FackClock();

    @BeforeEach
    void setUp() {
        LocalDateTime dateTime = LocalDateTime.parse("2024-02-21T21:00:00");
        clock.setCurrentDate(dateTime);
    }

    @Test
    void shouldCreateACouponAndCalculateTheDiscount() {
        var coupon = MyCoupon.create("SAVE10", 10, 2, LocalDate.of(2024, 03, 21), clock.getCurrentDate().toLocalDate());
        var discount = coupon.calcDiscountIn(100);

        assertThat(coupon.isAvailable()).isTrue();
        assertThat(coupon.getExpiredAt()).isAfter(clock.getCurrentDate().toLocalDate());
        assertThat(clock.getCurrentDate().toLocalDate()).isBefore(coupon.getExpiredAt());

        assertEquals(10, discount);
        assertEquals(2, coupon.getQuantity());
        assertEquals(1, coupon.getUsed());
        assertEquals(true, coupon.isAvailable());
    }

    @Test
    void shouldNotBeAbleToCreateAnCouponWithInvalidExpiredDate() {
        var invalidExDate = LocalDate.of(2024, 01, 21);
        assertThrows(InvalidCouponExpireDate.class,
                () -> MyCoupon.create("SAVE10", 10, 2, invalidExDate, clock.getCurrentDate().toLocalDate()));
    }

    @Test
    void shouldNotBeAbleIncreaseUsageIfReachItsLimit() {
        var coupon = MyCoupon.create("SAVE10", 10, 2, LocalDate.of(2024, 03, 21), clock.getCurrentDate().toLocalDate());
        coupon.calcDiscountIn(100);
        coupon.calcDiscountIn(100);
        assertThrows("This coupon reached its usage limite: SAVE10", CouponUsageLimitEx.class,
                () -> coupon.calcDiscountIn(100));
    }

    @Test
    void shouldChangeTheStatusOnceReachItsUsageLimit() {
        var coupon = MyCoupon.create("SAVE10", 10, 2, LocalDate.of(2024, 03, 21), clock.getCurrentDate().toLocalDate());
        coupon.calcDiscountIn(100);
        coupon.calcDiscountIn(100);

        assertEquals(coupon.getUsed(), 2);
        assertEquals(coupon.isAvailable(), false);
    }

    @Test
    void shouldValideteACouponExpiredDate() {
        var coupon = MyCoupon.create("SAVE10", 10, 2, LocalDate.of(2024, 03, 21), clock.getCurrentDate().toLocalDate());
        var expiredDate = LocalDate.of(2024, 04, 21);
        var NotExpiredDate = LocalDate.of(2024, 02, 21);
        assertTrue(coupon.isValid(NotExpiredDate));
        assertFalse(coupon.isValid(expiredDate));
    }

    @Test
    void shouldDecreaseTheUsageOfACoupon() {
        var coupon = MyCoupon.create("SAVE10", 10, 2, LocalDate.of(2024, 03, 21), clock.getCurrentDate().toLocalDate());
        coupon.calcDiscountIn(100);
        assertEquals(1, coupon.getUsed());
        coupon.decreseUsage(); /* back to 0 usages */
        assertEquals(0, coupon.getUsed());
    }

    @Test
    void shouldNotBeAbleToDecreaseOrIncreaseAnInvalidUsage() {
        var coupon = MyCoupon.create("SAVE10", 10, 2, LocalDate.of(2024, 03, 21), clock.getCurrentDate().toLocalDate());
        assertThrows("This coupon reached its usage limite: SAVE10", CouponUsageLimitEx.class,
                () -> coupon.decreseUsage()); /* back to 0 usages */
        coupon.calcDiscountIn(100);
        coupon.calcDiscountIn(100);
        assertThrows("This coupon reached its usage limite: SAVE10", CouponUsageLimitEx.class,
                () -> coupon.calcDiscountIn(100)); /* back to 0 usages */
    }

    @Test
    void shouldGenerateTheDiscountBaseOnTheCode() {
        var INVALID_FORMAT = "SAVE-10";
        var ex = assertThrows(RuntimeException.class, () -> MyCoupon.create(INVALID_FORMAT, 10, 2,
                LocalDate.parse("2024-03-21"), clock.getCurrentDate().toLocalDate()));

        assertEquals("Invalid Coupon Code, The coupon must starts with SAVE follow by the percentage discount!",
                ex.getMessage());
    }

    @Test
    void shouldCreateACouponWithBasedOnThePercentage() {
        var coupon = MyCoupon.create(null, 20, 2, LocalDate.parse("2024-03-21"), clock.getCurrentDate().toLocalDate());
        assertEquals("SAVE20", coupon.getCode());
    }

    @Test
    void s() {

    }

}
