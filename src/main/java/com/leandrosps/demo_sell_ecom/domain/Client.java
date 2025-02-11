package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Client {
   UUID id;  
   String name;
   String email;
   String city;
   LocalDate birthday;
   LocalDateTime createAt;
}