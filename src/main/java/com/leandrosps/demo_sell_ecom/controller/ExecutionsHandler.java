package com.leandrosps.demo_sell_ecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

@ControllerAdvice
public class ExecutionsHandler extends ResponseEntityExceptionHandler {

    private record HttpExeptionFormater(String type, String message) {
    }
    @ExceptionHandler(NotFoundEx.class)
    private ResponseEntity<HttpExeptionFormater> notfound(NotFoundEx nfx) {
        var exFormatter = new HttpExeptionFormater(nfx.getType(), nfx.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exFormatter);
    }
}
