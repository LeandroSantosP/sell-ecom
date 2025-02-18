package com.leandrosps.demo_sell_ecom.domain;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.leandrosps.demo_sell_ecom.geteways.MyClock;

@Tag("unit")
public class OrderTest {
   static Address addressDefault = new Address("MG", "Itaquera", null);
   static List<OrderItem> orderItems = new ArrayList<>();
   static {
      addressDefault.setCity("MG");
      orderItems.add(new OrderItem("123", 100, "123", 2));
      orderItems.add(new OrderItem("124", 20, "123", 2));
   }

   private MyClock clock = new MyClock() {

      private LocalDateTime date = LocalDateTime.of(2024, 02, 21, 0, 0);

      @Override
      public void setCurrentDate(LocalDateTime date) {
         this.date = date;
      }

      @Override
      public LocalDateTime getCurrentDate() {
         return this.date;
      };
   };

   @Test
   void shouldCreateAnValidTest() {

      Order order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.br", clock);
      orderItems.forEach(item -> order.addItem(item.unityPrice(), item.quantity(), item.productId()));
      assertEquals(240, order.calcTotal(addressDefault), "The total of the order is incorrect!");
      assertInstanceOf(Long.class, order.getTotal());
      assertInstanceOf(Long.class, order.getTotal());
   }

   @Test
   void shouldBeAbleToAddAnCupomOf20off() {
      var order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.", clock);
      order.addItems(orderItems.toArray(new OrderItem[0]));
      int percentage = 20;
      var coupon = MyCoupon.craete("SAVE20", percentage, true, 2, LocalDate.of(2024, 03, 21));

      order.addCoupon(coupon);
      assertEquals(192, order.calcTotal(addressDefault), "The total of the order is incorrect!");
   }

   @Test
   void shouldChangerStateFess() {
      Address adress = new Address("SP", "Itaquera", null); // 5.0
      var order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.", clock);
      order.addItems(orderItems.toArray(new OrderItem[0]));
      assertEquals(252, order.calcTotal(adress), "The total of the order is incorrect!");
   }

   @Test
   void shouldBeAbleToChangeTheStausOrder() {
      var order = Order.create(UUID.randomUUID().toString(), "joao@exemple.com.", clock);
      assertEquals("WAITING_PAYMENT", order.getStatus());

      order.updated_status("PAID");
      assertEquals("PAID", order.getStatus());

      order.updated_status("RECUSSED");
      assertEquals("RECUSSED", order.getStatus());

      assertThrows("Invalid Status!", RuntimeException.class, () -> order.updated_status("INVALID_STATUS"));
      assertEquals("RECUSSED", order.getStatus());

   }

   @ParameterizedTest
   @DisplayName("╯°□°）╯")
   @ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
   void palindromes(String candidate) {
   }
}
