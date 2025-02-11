package com.leandrosps.demo_sell_ecom.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
public class OrderTest {
   static Address addressDefault = new Address("MG", "Itaquera", null);
   static List<OrderItem> orderItems = new ArrayList<>();
   static {
      addressDefault.setCity("MG");
      orderItems.add(new OrderItem("123", 100, "123", 2));
      orderItems.add(new OrderItem("124", 20, "123", 2));
   }

   @Test
   void shouldCreateAnValidTest() {
      Order order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.br", LocalDateTime.now());
      orderItems.forEach(item -> order.addItem(item.unityPrice(), item.quantity(), item.productId()));
      assertEquals(240, order.calcTotal(addressDefault), "The total of the order is incorrect!");
      assertInstanceOf(Long.class, order.getTotal());
      assertInstanceOf(Long.class, order.getTotal());
   }

   @Test
   void shouldBeAbleToAddAnCupomOf20off() {
      var order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.", LocalDateTime.now());
      order.addItems(orderItems.toArray(new OrderItem[0]));
      double percentage = 20;
      order.addCoupon("SAVE20", percentage);
      assertEquals(192, order.calcTotal(addressDefault), "The total of the order is incorrect!");
   }

   @Test
   void shouldChangerStateFess() {
       Address adress = new Address("SP", "Itaquera", null); // 5.0
      var order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.", LocalDateTime.now());
      order.addItems(orderItems.toArray(new OrderItem[0]));
      assertEquals(252, order.calcTotal(adress), "The total of the order is incorrect!");
   }

   @ParameterizedTest
   @DisplayName("╯°□°）╯")
   @ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
   void palindromes(String candidate) {
   }

}
