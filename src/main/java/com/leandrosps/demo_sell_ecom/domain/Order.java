package com.leandrosps.demo_sell_ecom.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.leandrosps.demo_sell_ecom.infra.errors.ExpiredCoupon;
import com.leandrosps.demo_sell_ecom.infra.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.infra.geteways.MyClock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Order {
   private String id;
   private String clientId;
   private String clientEmail;
   private Status status;
   private List<OrderItem> orderItems;
   private List<MyCoupon> coupons;
   private long total;
   private LocalDateTime createAt;
   private MyClock clock;

   public static Order create(String clientId, String clientEmail, MyClock clock) {
      var inital_status = Status.WAITING_PAYMENT;

      return new Order(UUID.randomUUID().toString(), clientId, clientEmail, inital_status, new ArrayList<>(),
            new ArrayList<>(), 0L, clock.getCurrentDate(), clock);
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
      if (!coupon.isValid(this.getCreateAt().toLocalDate())) {
         throw new ExpiredCoupon();
      }
      this.coupons.add(coupon);
   }

   public Long calcTotal(Address address) {

      if (this.getStatus().equals("CANCEL")) {
         throw new RuntimeException("This order has been cancel!");
      }

      Long total = 0L;

      for (var item : this.orderItems) {
         total += item.unityPrice() * item.quantity();
      }
      var feesCalculator = StateFessCalculatorFactory.create(address.getState());
      var stateFees = feesCalculator.calctateStateFees(total.doubleValue());
      total += (long) stateFees;

      for (MyCoupon coupon : this.coupons) {
         total -= (long) coupon.calcDiscountIn(total);
      }

      this.total = total;
      return total;
   }

   public String getStatus() {
      return this.status.name();
   }

   public static boolean isValidSatus(String status) {
      var VALID_STATUS = List.of("PAID", "RECUSSED", "CANCEL","WAITING_PAYMENT");
      return VALID_STATUS.contains(status) ? true : false;
   }

   public void updated_status(String status) {
      if (!isValidSatus(status)) {
         throw new RuntimeException("Invalid Status!");
      }

      if (status.equals("PAID")) {
         this.status = Status.PAID;
         return;
      } else if (status.equals("RECUSSED")) {
         this.status = Status.RECUSSED;
         return;
      } else if (status.equals("CANCEL")) {
         this.status = Status.CANCEL;
         return;
      }
   }

   public void cancel () {
      if (this.getStatus().equals("PAID")) {
         throw new RuntimeException("This order has already been paid!");
      }

      if (this.getStatus().equals("WAITING_PAYMENT")) {
         this.updated_status("CANCEL");
      }

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
