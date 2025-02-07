package com.leandrosps.demo_sell_ecom.db;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDbModel(String id, String client_id, List<OrderItemDbModel> order_items, String client_email,
        String status, long total, LocalDateTime created_at) {
}