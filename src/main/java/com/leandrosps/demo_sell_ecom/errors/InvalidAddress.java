package com.leandrosps.demo_sell_ecom.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAddress extends RuntimeException {
    private ERROTYPE type = ERROTYPE.DEFAULT;

    public InvalidAddress(String code) {
        super("Invalid Adress Code: " + code);
    }

    public String getType() {
        return this.type.toString();
    }
}
