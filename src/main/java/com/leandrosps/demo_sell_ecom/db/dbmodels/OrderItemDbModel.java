package com.leandrosps.demo_sell_ecom.db.dbmodels;

public record OrderItemDbModel(String id, long unity_price, int quantity, String order_id, String product_id) {
}

// String id, long unityPrice, String productId, int quantity, String orderId