package com.leandrosps.demo_sell_ecom.infra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.services.OrderService;
import com.leandrosps.demo_sell_ecom.application.services.OrderService.ItemInputs;
import com.leandrosps.demo_sell_ecom.infra.config.SecurityConfig;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Orders", description = "Controller to create/get new orders!")
@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class OrderController {
    private OrderService orderService;
    private OrderQueryService orderQueryService;

    public OrderController(OrderService orderService, OrderQueryService orderQueryService) {
        this.orderService = orderService;
        this.orderQueryService = orderQueryService;
    }

    public record ListItem(String procuct_id, int quantity) {
    }

    public record ReguesBody(String email, String addressCode, String getewaytoken, List<ListItem> items) {
    }

    public record InputOrderController(@NotNull String email, @NotNull List<ItemInputs> items,
            @NotNull String getewaytoken, @NotNull String addressCode, String coupon) {
    }

    @Operation(summary = "Create news Order.", description = "Usecase for that users can places orders.")
    @ApiResponse(responseCode = "201", description = "Orderd created with success!")
    @ApiResponse(responseCode = "404", description = "Product not founded!")
    @ApiResponse(responseCode = "400", description = "Invalid request: Address is not valid, coupon has expired, or state is invalid.")
    @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    @PostMapping("/place-order")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody InputOrderController input) {
        String product_id = this.orderService.placeOrder(input.email(), input.items(), input.addressCode(),
                input.coupon());
        return ResponseEntity.status(HttpStatus.CREATED).body(product_id);
    }

    public record MakePaymenBody(@NotNull String payment_token) {
    }

    @Operation(summary = "Make the payment.", description = "Usecase for that users can make the order payment..")
    @ApiResponse(responseCode = "200", description = "Processing the payment!")
    @ApiResponse(responseCode = "404", description = "Order not founded!")
    @ApiResponse(responseCode = "400", description = "Invalid request: Address is not valid, coupon has expired, or state is invalid.")
    @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    @PostMapping("/make-payment/{order_id}")
    public ResponseEntity<ReguesBody> makePayment(@NotNull @PathVariable String order_id,
            @RequestBody MakePaymenBody body) {
        this.orderService.makePayment(order_id, body.payment_token());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel-order/{order_id}")
    public ResponseEntity<?> cancelOrder(@NotNull @PathVariable String order_id) {
        this.orderService.cancelOrder(order_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{client_id}/{status}")
    public ResponseEntity<?> getMethodName(@PathVariable String client_id, @PathVariable String status) {
        var result = orderQueryService.getOrdersOfAClient(client_id, status);
        return ResponseEntity.ok(result);
    }
}
