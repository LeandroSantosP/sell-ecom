package com.leandrosps.demo_sell_ecom.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.OrderService;
import com.leandrosps.demo_sell_ecom.application.OrderService.ItemInputs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public record InnerOrderController(@NotEmpty String email, @NotEmpty List<ItemInputs> items) {
    }

    @PostMapping("/place-order")
    public void placeOrder(@RequestBody InnerOrderController input) {
        var placeOrder = this.orderService.placeOrder(input.email(), input.items());
        return;
    }
    
}
