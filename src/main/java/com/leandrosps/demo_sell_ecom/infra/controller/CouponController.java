package com.leandrosps.demo_sell_ecom.infra.controller;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Coupons", description = "Controller to create/get new coupons!")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

	private CouponService couponService;

	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@Operation(summary = "Create news coupons!", description = "UseCase for admins can create new coupons so that users can add this coupons in the orders!")
	@ApiResponse(responseCode = "200", description = "Coupon finded!")
	@ApiResponse(responseCode = "404", description = "Coupon not founded!!")
	@ApiResponse(responseCode = "500", description = "Internal Server Error!")
	@GetMapping("/{code}")
	public ResponseEntity<GetCouponOutput> getcoupon(@PathVariable String code) {
		var coupon = this.couponService.getCoupon(code);
		return ResponseEntity.status(HttpStatus.OK).body(coupon);
	}

	@Operation(summary = "Create news coupons!", description = "UseCase for admins can create new coupons so that users can add this coupons in the orders!")
	@ApiResponse(responseCode = "201", description = "Coupon created with success!")
	@ApiResponse(responseCode = "400", description = "This coupon has already been created!")
	@ApiResponse(responseCode = "500", description = "Internal Server Error!")
	@PostMapping("/")
	@PreAuthorize("hasRole('admin')")
	public ResponseEntity<Void> createcoupon(@Valid @RequestBody CreateCouponInput input,
			@AuthenticationPrincipal Jwt jwt) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication.isAuthenticated());
		System.out.println(authentication.getAuthorities());
		this.couponService.create(input);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
