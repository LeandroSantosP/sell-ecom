package com.leandrosps.demo_sell_ecom.domain;

public record OrderItem(String id, long unityPrice, String productId, int quantity) {
}
