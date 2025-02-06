package com.leandrosps.demo_sell_ecom.domain;

public record OrderItem(String productId, long unityPrice, int quantity) {
}