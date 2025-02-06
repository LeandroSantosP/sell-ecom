package com.leandrosps.demo_sell_ecom.errors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundEx extends RuntimeException {
    public NotFoundEx(String mss){
        super(mss);
    }

    public NotFoundEx() {
        super();
    }
}
