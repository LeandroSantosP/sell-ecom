package com.leandrosps.demo_sell_ecom.infra.db.dbmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemDbModel {
    private String id;
    private long unity_price;
    private int quantity;
    private String order_id;
    private String product_id;
}


// String id, long unityPrice, String productId, int quantity, String orderId