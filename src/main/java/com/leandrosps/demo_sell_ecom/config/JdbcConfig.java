package com.leandrosps.demo_sell_ecom.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "com.leandrosps.demo_sell_ecom")
public class JdbcConfig {
}
