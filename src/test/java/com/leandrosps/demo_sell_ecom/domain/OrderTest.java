package com.leandrosps.demo_sell_ecom.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OrderTest {
   @Test
   @Disabled
   void shouldCreateAnValidTest() {
      ArrayList<OrderItem> orderItems = new ArrayList<>();
      //String id, long unityPrice, String productId, int quantity, String orderId
      orderItems.add(new OrderItem("123", 100,"", 2, ""));
      orderItems.add(new OrderItem("124", 20,"", 2, ""));
      Order order = Order.create(UUID.randomUUID().toString(),"joao@exemple.com.br", LocalDateTime.now());
      orderItems.forEach(item -> order.addItem(item.productId(), item.unityPrice(), item.quantity()));

      assertEquals(order.calcTotal(), 240, "The total of the order is incorrect!");
      assertInstanceOf(Long.class, order.getTotal());
      assertInstanceOf(Long.class, order.getTotal());
   }

   @ParameterizedTest
   @DisplayName("╯°□°）╯")
   @ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
   void palindromes(String candidate) {
   }
}
