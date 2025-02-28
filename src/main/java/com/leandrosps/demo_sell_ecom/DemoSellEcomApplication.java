package com.leandrosps.demo_sell_ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Sell-Ecom API", version = "1", description = "Api for sell-ecom application an ecomercer api!"))
public class DemoSellEcomApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSellEcomApplication.class, args);
	}

}
