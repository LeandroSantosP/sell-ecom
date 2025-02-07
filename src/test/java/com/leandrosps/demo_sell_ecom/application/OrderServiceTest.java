package com.leandrosps.demo_sell_ecom.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.leandrosps.demo_sell_ecom.application.OrderService.ItemInputs;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void testPlaceOrder() {
        List<ItemInputs> itemsInput = new ArrayList<>();
        itemsInput.add(new ItemInputs("284791a5-5a40-4a31-a60c-d2df68997569", 3));
        var createOrderOutput = this.orderService.placeOrder("joao@exemplo.com.br", itemsInput);
        assertInstanceOf(UUID.class, UUID.fromString(createOrderOutput));
        var output = this.orderService.getOrder(createOrderOutput);

        assertEquals("joao@exemplo.com.br", output.clientEmail());
        assertEquals(3009, output.total());
        assertEquals("wating_payment", output.status());
    }
}
