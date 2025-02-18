package com.leandrosps.demo_sell_ecom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.leandrosps.demo_sell_ecom.handlers.PaymentAcceptHandler;
import com.leandrosps.demo_sell_ecom.handlers.PaymentRecussedHandler;
import com.leandrosps.demo_sell_ecom.handlers.ProductStatusUpdatedHandler;
import com.leandrosps.demo_sell_ecom.infra.mediator.Mediator;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {
	
	@Autowired
	private Mediator mediator;

	@Autowired
	PaymentAcceptHandler paymentAcceptHandler;

	@Autowired
	PaymentRecussedHandler paymentRecussedHandler;

	@Autowired
	ProductStatusUpdatedHandler productStatusUpdatedHandler;

	@PostConstruct
	public void registerHandlers(){
		log.info("RegisterHandlers");
		this.mediator.register(paymentAcceptHandler);
		this.mediator.register(paymentRecussedHandler);
		this.mediator.register(productStatusUpdatedHandler);
	}
}
