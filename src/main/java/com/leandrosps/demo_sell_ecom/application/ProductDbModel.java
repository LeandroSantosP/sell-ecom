package com.leandrosps.demo_sell_ecom.application;

public record ProductDbModel(String id, String name, Long price, Integer stoke, String brand_id, String img_ref) {
}