package com.leandrosps.demo_sell_ecom.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GetewayServerError extends RuntimeException {
	private ERROTYPE type = ERROTYPE.DEFAULT;

	public GetewayServerError(String mss, String geteway) {
		super("Error on Geteway Server: " + geteway + " " + mss);
	}

	public String getType() {
		return this.type.toString();
	}
}
