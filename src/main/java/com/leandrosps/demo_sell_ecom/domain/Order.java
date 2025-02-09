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
   private String clientEmail;
   private Status status;
   private LocalDateTime orderDate;
   private List<OrderItem> orderItems;
   private Long total;

   public static Order create(String clientId, String clientEmail, LocalDateTime orderDate) {
      if (orderDate == null) {
         orderDate = LocalDateTime.now();
      }
      var inital_status = Status.WAITING_PAYMENT;
      return new Order(UUID.randomUUID().toString(), clientId, clientEmail, inital_status, orderDate, new ArrayList<>(),
            0L);
   }

   public void addItem(String productId, long unityPrice, int quantity) {
      String id = UUID.randomUUID().toString();
      this.orderItems.add(new OrderItem(id, unityPrice, productId, quantity));
   }

   public Long calcTotal() {
      var total = 0L;
      for (var item : this.orderItems) {
         total += item.unityPrice() * item.quantity();

      }
      this.total = total;
      return total;
   }

   public String getStatus(){
      return this.status.name();
   }

}
