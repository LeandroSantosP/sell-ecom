package com.leandrosps.demo_sell_ecom.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCouponExpireDate extends IllegalArgumentException {

	private ERROTYPE type = ERROTYPE.HANDLE;

	public InvalidCouponExpireDate() {
		super("Ivalid Coupon ExpiredDate!");
	}

	public String getType() {
		return this.type.toString();
	}
	
}
