package com.leandrosps.demo_sell_ecom.errors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

enum ERROTYPE {
    DEFAULT
}

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundEx extends RuntimeException {
     private ERROTYPE type = ERROTYPE.DEFAULT;

    public NotFoundEx(String mss){
        super(mss);
    }
    public NotFoundEx() {
        super();
    }

    public String getType(){
        return this.type.toString();
    }
}
