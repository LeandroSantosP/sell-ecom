package com.leandrosps.demo_sell_ecom.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.db.dbmodels.ClientDbModel;
import com.leandrosps.demo_sell_ecom.db.dbmodels.OrderItemDbModel;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

@Service
public class OrderQueryService {

    private JdbcClient jdbcClient;

    private OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository, JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        this.orderRepository = orderRepository;
    }

    public record GetOrderData(String orderId, String clientId, String clientName, String clientEmail, Long orderTotal,
            String orderStatus, LocalDateTime orderCreatedAt) {
    }

    public record GetOrdersOfAClientOutPut(String orderId, String clientId, String clientName, String clientEmail,
            Long orderTotal, String orderStatus, LocalDateTime orderCreatedAt, List<OrderItemDbModel> orderItems) {
    }

    public record GetOrderByIdOuput() {
    }

    public GetOrderByIdOuput getOrderById(String order_id) {
        var orderData = orderRepository.findById(order_id).orElseThrow(() -> new NotFoundEx("Order Not exists!"));
        List<OrderItemDbModel> orderItems = jdbcClient.sql("SELECT * FROM order_items WHERE order_id = :order_id")
                .param("order_id", orderData.id()).query(OrderItemDbModel.class).list();

        return null;

    }

    public List<GetOrdersOfAClientOutPut> getOrdersOfAClient(String client_id, String status) {
        jdbcClient.sql("SELECT * FROM clients WHERE id = :id").param("id", client_id).query(ClientDbModel.class)
                .optional().orElseThrow(() -> new NotFoundEx("Invalid Client!"));

        var sql = """
                SELECT
                    o.id AS orderId,
                    c.id AS clientId,
                    c.name AS clientName,
                    c.email AS clientEmail,
                    o.total as orderTotal,
                    o.status as orderStatus,
                    o.created_at as orderCreatedAt
                FROM
                    clients AS c
                    INNER JOIN orders AS o ON (c.id = o.client_id)
                WHERE
                    c.id = :client_id AND o.status = :status;
                                                """;
        List<GetOrderData> orders = jdbcClient.sql(sql).param("client_id", client_id).param("status", status)
                .query(GetOrderData.class).list();

        List<GetOrdersOfAClientOutPut> result = new ArrayList<>();

        orders.forEach(order -> {
            List<OrderItemDbModel> orderItems = jdbcClient.sql("SELECT * FROM order_items WHERE order_id = :order_id")
                    .param("order_id", order.orderId()).query(OrderItemDbModel.class).list();

            result.add(new GetOrdersOfAClientOutPut(order.orderId(), order.clientId(), order.clientName(),
                    order.clientEmail(), order.orderTotal(), order.orderStatus(), order.orderCreatedAt(), orderItems));
        });

        return result;
    }

}
