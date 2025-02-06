package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Order {
   private String id;
   private String clientId;
   private LocalDateTime orderDate;
   private List<OrderItem> orderItems;
   private Long total;

   
   public static Order create(String clientId, LocalDateTime orderDate) {
      if (orderDate == null) {
         orderDate = LocalDateTime.now();
      }
      return new Order(UUID.randomUUID().toString(), clientId, orderDate, new ArrayList<>(), 0L);

   }

   public void addItem(String productId, long unityPrice, int quantity){
      this.orderItems.add(new OrderItem(productId, unityPrice, quantity));
   }

   public Long calcTotal() {
      var total = 0L;
      for (var item : this.orderItems) {
         total += item.unityPrice() * item.quantity();

      }
      return total;
   }

}
