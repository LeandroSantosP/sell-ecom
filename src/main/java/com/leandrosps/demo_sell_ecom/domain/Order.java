package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

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
   private List<MyCoupon> coupons;
   private Long total;

   public static Order create(String clientId, String clientEmail) {
      var inital_status = Status.WAITING_PAYMENT;

      return new Order(UUID.randomUUID().toString(), clientId, clientEmail, inital_status,  LocalDateTime.now(), new ArrayList<>(),
            new ArrayList<>(), 0L);
   }

   public void addItem(long unityPrice, int quantity, String productId) {
      String id = UUID.randomUUID().toString();
      this.orderItems.add(new OrderItem(id, unityPrice, productId, quantity));
   }

   public void addItems(OrderItem... orderItem) {
      for (OrderItem item : orderItem) {
         this.orderItems.add(item);
      }
   }

   public void addCoupon(MyCoupon coupon) {
      this.coupons.add(coupon);
   }

   public Long calcTotal(Address address) {
      Long total = 0L;

      for (var item : this.orderItems) {
         total += item.unityPrice() * item.quantity();
      }
      var feesCalculator = StateFessCalculatorFactory.create(address.getState());
      var stateFees = feesCalculator.calctateStateFees(total.doubleValue());
      total += (long) stateFees;

      for (MyCoupon coupon : this.coupons) {
         total -= (long) coupon.calcDiscountIn(total);
         coupon.increaseUsage();
      }
      
      this.total = total;
      return total;
   }

   public String getStatus() {
      return this.status.name();
   }

}

class StateFessCalculatorFactory {
   static StateFessCalculator create(String state) {

      if (state.equals("SP")) {
         return new StateFessCalculatorSp();
      }

      if (state.equals("RJ")) {
         return new StateFessCalculatorRj();
      }

      if (state.equals("MG")) {
         /* No fees for MG */
         return new StateFessCalculator() {
            @Override
            public double calctateStateFees(Double value) {
               return 0;
            }
         };
      }

      throw new NotFoundEx("Invalid State!");
   }

}

interface StateFessCalculator {
   abstract double calctateStateFees(Double value);
}

class StateFessCalculatorSp implements StateFessCalculator {
   double iof_percentage = 5.0;

   @Override
   public double calctateStateFees(Double value) {
      var iof = (value * iof_percentage) / 100;
      return iof;
   }
}

class StateFessCalculatorRj implements StateFessCalculator {
   double iof_percentage = 2.0;

   @Override
   public double calctateStateFees(Double value) {
      var iof = (value * iof_percentage) / 100;
      return iof;
   }
}
