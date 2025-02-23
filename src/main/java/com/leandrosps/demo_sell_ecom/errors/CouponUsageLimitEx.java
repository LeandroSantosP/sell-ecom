package com.leandrosps.demo_sell_ecom.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CouponUsageLimitEx extends RuntimeException {
    private ERROTYPE type = ERROTYPE.HANDLE;

    public CouponUsageLimitEx(String code){
        super("This coupon reach its usage limite: " + code);
    }
    public String getType(){
        return this.type.toString();
    }
}
