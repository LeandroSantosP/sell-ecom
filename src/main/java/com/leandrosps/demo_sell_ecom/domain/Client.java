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


/* 
 * 
 * 
 * 
 * 
    @Autowired
    OrderService orderService;

    @Test
    void testPlaceOrder() {
        var itemsInput = new ArrayList<OrderItem>();
        itemsInput.add(new OrderItem("123", 2));
        itemsInput.add(new OrderItem("1234", 4));
        var orderId = this.orderService.placeOrder("joao@exemplo.com.br", null);
        assertInstanceOf(UUID.class, UUID.fromString("orderId"));
    }





      @Test
   void shouldPlaceAnOrder(){
      var orderItems = new ArrayList<OrderItem>();
      orderItems.add(new OrderItem("123", 1200, 3));
      orderItems.add(new OrderItem("124", 1002, 2));

      var order = Order.create("1234", LocalDateTime.now(), orderItems);

      assertEquals(0,order.getTotal());
      assertEquals(5604,order.calcTotal());
   }

   @Test
   void test() {
      System.out.println(UUID.randomUUID());
   }

   
 */