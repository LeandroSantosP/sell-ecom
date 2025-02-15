package com.leandrosps.demo_sell_ecom.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.leandrosps.demo_sell_ecom.domain.Address;
import com.leandrosps.demo_sell_ecom.geteways.AdressGeteWay;

@Configuration 
public class TestConfig {
    
    @Bean
    @Qualifier("testHttpClientGetewaySp")
    public AdressGeteWay testAdressGeteWay() {
        return new AdressGeteWay() {
            @Override
            public Address getAdress(String code) {
                return new Address("SP", "Itaquera", null); // 5.0
            }
        }; 
    }
}
