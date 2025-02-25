package com.leandrosps.demo_sell_ecom.infra.errors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

enum ERROTYPE {
    HANDLE,
    NOT_CUSTOM, 
    INTERNAL
}

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundEx extends RuntimeException {
     private ERROTYPE type = ERROTYPE.HANDLE;

    public NotFoundEx(String mss){
        super(mss);
    }
    public NotFoundEx() {
        super();
    }

    public ERROTYPE getType(){
        return this.type;
    }
}
