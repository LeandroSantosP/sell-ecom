package com.leandrosps.demo_sell_ecom.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExecutionsHandler extends ResponseEntityExceptionHandler {

    private record HttpExeptionFormater(String type, String message) {
    }

    @ExceptionHandler(NotFoundEx.class)
    private ResponseEntity<HttpExeptionFormater> notfound(NotFoundEx nfx) {
        var exFormatter = new HttpExeptionFormater(nfx.getType(), nfx.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exFormatter);
    }

    @ExceptionHandler(GetewayServerError.class)
    private ResponseEntity<HttpExeptionFormater> getewayServerError(GetewayServerError ex) {
        var exFormatter = new HttpExeptionFormater(ex.getType(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exFormatter);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<HttpExeptionFormater> runTimeEx(Exception ex){
        ex.printStackTrace();
        var exFormatter = new HttpExeptionFormater("RuntimeException", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exFormatter);
    }
}
