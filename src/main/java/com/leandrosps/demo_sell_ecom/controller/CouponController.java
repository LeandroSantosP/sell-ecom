package com.leandrosps.demo_sell_ecom.controller;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.services.CouponService;
import com.leandrosps.demo_sell_ecom.application.services.CouponService.CreateCouponInput;
import com.leandrosps.demo_sell_ecom.application.services.CouponService.GetCouponOutput;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

	private CouponService couponService;

	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@GetMapping("/{code}")
	public ResponseEntity<GetCouponOutput> getcoupon(@PathVariable String code) {
		var coupon = this.couponService.getCoupon(code);
		return ResponseEntity.status(HttpStatus.OK).body(coupon);
	}

	@PostMapping("/")
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<Void> createcoupon(@Valid @RequestBody CreateCouponInput input,
			@AuthenticationPrincipal Jwt jwt) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (jwt != null) {
			System.out.println("jwt: " + jwt.getClaimAsString("user_roles"));
		}
		System.out.println(authentication.isAuthenticated());
		System.out.println(authentication.getAuthorities());
		this.couponService.create(input);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
