package com.leandrosps.demo_sell_ecom.infra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosps.demo_sell_ecom.application.auth.UserCustom;
import com.leandrosps.demo_sell_ecom.application.services.OrderService;
import com.leandrosps.demo_sell_ecom.application.services.OrderService.ItemInputs;
import com.leandrosps.demo_sell_ecom.infra.config.AppConfig;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService.GetOrderByIdOuput;
import com.leandrosps.demo_sell_ecom.query.OrderQueryService.GetOrdersOfAClientOutPut;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Orders", description = "Controller to create/get new orders!")
@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = AppConfig.SECURITY_BEARER)
@SecurityRequirement(name = AppConfig.SECURITY_BASIC)
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
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user_email = getUserEmail(principal);
        String product_id = this.orderService.placeOrder(user_email, input.items(), input.addressCode(),
                input.coupon());
        return ResponseEntity.status(HttpStatus.CREATED).body(product_id);
    }

    public record MakePaymenBody(@NotNull String payment_token) {
    }

    public static String getUserEmail(Object principal) {
        if (principal instanceof UserCustom) {
            var credentials = (UserCustom) principal;
            return credentials.getUserEmail();
        } else if (principal instanceof Jwt) {
            var jwt = (Jwt) principal;
            return jwt.getClaim("user_email");
        }
        throw new RuntimeException("Authentication principal not supported!");
    }

    @Operation(summary = "Get an Order", description = "Usecase for get a order by its id and status.")
    @ApiResponse(responseCode = "200", description = "Order founded!")
    @ApiResponse(responseCode = "404", description = "Order not founded!")
    @ApiResponse(responseCode = "400", description = "Ivalid order Status!")
    @GetMapping("/get-order/{order_id}")
    public ResponseEntity<GetOrderByIdOuput> getOrderById(@Valid @PathVariable String order_id) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user_email = getUserEmail(principal);
        var output = this.orderQueryService.getOrder(order_id, user_email);
        return ResponseEntity.ok().body(output);
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

    @Operation(summary = "Cancel a Order!", description = "Use Case for cancel a order!!")
    @ApiResponse(responseCode = "204", description = "Order Canceled!")
    @ApiResponse(responseCode = "400", description = "This order has already been paid!")
    @ApiResponse(responseCode = "500", description = "Internal Server Error!")
    @PostMapping("/cancel-order/{order_id}")
    public ResponseEntity<?> cancelOrder(@NotNull @PathVariable String order_id) {
        this.orderService.cancelOrder(order_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "List client orders!", description = "Use Case to get a order, base on the client id and order status!")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order Returned!"),
            @ApiResponse(responseCode = "400", description = "Order not founded!"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error!") })
    @GetMapping("/{client_id}/{status}")
    public ResponseEntity<List<GetOrdersOfAClientOutPut>> getMethodName(@PathVariable String client_id,
            @PathVariable String status) {
        var result = orderQueryService.getOrdersOfAClient(client_id, status);
        return ResponseEntity.ok(result);
    }
}
