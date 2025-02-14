package com.leandrosps.demo_sell_ecom.query;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
public class CouponQueryService {
    
    private JdbcClient jdbcClient;

    public CouponQueryService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    
}
