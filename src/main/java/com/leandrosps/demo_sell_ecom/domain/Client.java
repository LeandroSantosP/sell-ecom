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

   public static Client create(String name, String email, String password, String city, LocalDate birthday) {
      return new Client(UUID.randomUUID(), name, email, city, birthday, LocalDateTime.now());
   }

}