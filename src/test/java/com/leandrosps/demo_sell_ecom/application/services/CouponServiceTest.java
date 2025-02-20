package com.leandrosps.demo_sell_ecom.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.leandrosps.demo_sell_ecom.application.services.CouponService.CreateCouponInput;
import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.geteways.MyClock;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
public class CouponServiceTest {

	@Container
	@ServiceConnection
	static final MySQLContainer<?> mysqldb;

	static {
		mysqldb = new MySQLContainer<>("mysql:latest");
		mysqldb.withDatabaseName("testdb").withUsername("testuser").withPassword("testpass")
				.withInitScripts("schema_test.sql", "data_test.sql");
		mysqldb.start();
	}

	@Mock
	private MyClock clock;

	@Autowired
	@InjectMocks
	private CouponService couponService;

	@Autowired
	private CouponRepository couponRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		this.couponRepository.deleteAll();
	}

	public LocalDateTime currentDate = LocalDateTime.parse("2025-02-10T00:00:00");

	@Test
	void testGetCoupon() {
		Mockito.when(this.clock.getCurrentDate()).thenReturn(currentDate);
		this.couponService.create(new CreateCouponInput(null, 20, 0, LocalDate.parse("2025-02-11")));
		var coupon = this.couponService.getCoupon("SAVE20");
		assertThat(coupon.code()).isEqualTo("SAVE20");
		assertThat(coupon.percentage()).isEqualTo(20);
		assertThat(coupon.isAvealible()).isFalse();
		assertThat(coupon.expiredAt()).isAfter(currentDate.toLocalDate()).isInThePast();
	}
}
