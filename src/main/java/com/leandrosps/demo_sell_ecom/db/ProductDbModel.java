package com.leandrosps.demo_sell_ecom.db;

public record ProductDbModel(String id, String name, Long price, Integer stoke, String image_ref, String brand_id) {
}