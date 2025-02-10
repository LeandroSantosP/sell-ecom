package com.leandrosps.demo_sell_ecom.query;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.db.dbmodels.ClientDbModel;
import com.leandrosps.demo_sell_ecom.domain.OrderItem;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

@Service
public class OrderQueryService {

    private JdbcClient jdbcClient;

    public OrderQueryService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public record GetOrderOutput(String clientId, String clientName, String clientEmail, String orderId, Long orderTotal,
            String orderStatus, LocalDateTime orderCreatedAt) {
    }

    private List<OrderItem> orderItems;

    public record InnerOrderQuery() {
    }

    public List<GetOrderOutput> getOrdersOfAClient(String client_id, String status) {
        jdbcClient.sql("SELECT * FROM clients WHERE id = :id").param("id", client_id).query(ClientDbModel.class)
                .optional().orElseThrow(() -> new NotFoundEx("Invalid Client!"));

        var sql = """
                 SELECT
                    c.id AS clientId,
                    c.name AS clientName,
                    c.email AS clientEmail,
                    o.id AS orderId,
                    o.total as orderTotal,
                    o.status as orderStatus,
                    o.created_at as orderCreatedAt
                FROM
                    clients AS c
                    INNER JOIN orders AS o ON (c.id = o.client_id)
                    WHERE c.id = :client_id AND o.status = :status;
                                """;
        return jdbcClient.sql(sql).param("client_id", client_id).param("status", status).query(GetOrderOutput.class)
                .list();
    }

}
