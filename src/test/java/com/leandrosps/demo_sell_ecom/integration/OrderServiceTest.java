package com.leandrosps.demo_sell_ecom.integration;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.leandrosps.demo_sell_ecom.application.OrderService;
import com.leandrosps.demo_sell_ecom.application.OrderService.ItemInputs;

@SpringBootTest
public class OrderServiceTest {
    OrderService orderService;

    @Test
    void testPlaceOrder() {
        var itemsInput = new ArrayList<ItemInputs>();
        itemsInput.add(new ItemInputs("123", 2));
        itemsInput.add(new ItemInputs("1234", 4));
        var orderId = this.orderService.placeOrder("joao@exemplo.com.br", null);
        assertInstanceOf(UUID.class, UUID.fromString("orderId"));
    }
}
