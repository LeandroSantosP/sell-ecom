package com.leandrosps.demo_sell_ecom.infra.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpiredCoupon extends RuntimeException {
    private ERROTYPE type = ERROTYPE.HANDLE;

    public ExpiredCoupon(){
        super("Expired Coupon!");
    }
    public String getType(){
        return this.type.toString();
    }
}
