package com.leandrosps.demo_sell_ecom.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.OrderService;
import com.leandrosps.demo_sell_ecom.application.OrderService.ItemInputs;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;
    private OrderQueryService orderQueryService;

    public OrderController(OrderService orderService, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
    }

    public record InputOrderController(@NotNull String email, @NotNull List<ItemInputs> items,
            @NotNull String getewaytoken, @NotNull String addressCode, String coupon) {
    }

    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody InputOrderController input) {
        String product_id = this.orderService.placeOrder(input.email(), input.items(), input.getewaytoken(),
                input.addressCode(), input.coupon());
        return ResponseEntity.ok(product_id);
    }

    @GetMapping("/{client_id}/{status}")
    public ResponseEntity<?> getMethodName(@PathVariable String client_id, @PathVariable String status) {
        var result = orderQueryService.getOrdersOfAClient(client_id, status);
        return ResponseEntity.ok(result);
    }
}
