package com.leandrosps.demo_sell_ecom.application;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientDbModel(String id, String name, String email, String city, LocalDate birthday, LocalDateTime create_at) {
}