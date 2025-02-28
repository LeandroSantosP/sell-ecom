package com.leandrosps.demo_sell_ecom.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.leandrosps.demo_sell_ecom.handlers.PaymentAcceptHandler;
import com.leandrosps.demo_sell_ecom.handlers.PaymentRecussedHandler;
import com.leandrosps.demo_sell_ecom.infra.mediator.Mediator;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@SecurityScheme(name = AppConfig.SECURITY_BEARER, type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@SecurityScheme(name = AppConfig.SECURITY_BASIC, type = SecuritySchemeType.HTTP, scheme = "basic", description = "Basic for auth for production while development!")
public class AppConfig {
	
	public static final String SECURITY_BEARER = "Bearer Auth";
	public static final String SECURITY_BASIC = "Basic Auth";

	@Autowired
	private Mediator mediator;

	@Autowired
	PaymentAcceptHandler paymentAcceptHandler;

	@Autowired
	PaymentRecussedHandler paymentRecussedHandler;

	@PostConstruct
	public void registerHandlers(){
		log.info("RegisterHandlers");
		this.mediator.register(paymentAcceptHandler);
		this.mediator.register(paymentRecussedHandler);
	}
}
