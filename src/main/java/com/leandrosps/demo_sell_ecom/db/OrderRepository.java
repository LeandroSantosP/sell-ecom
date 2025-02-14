package com.leandrosps.demo_sell_ecom.db;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

import com.leandrosps.demo_sell_ecom.db.dbmodels.OrderDbModel;
import com.leandrosps.demo_sell_ecom.domain.Order;

public interface OrderRepository extends ListCrudRepository<OrderDbModel, String>, OrderRepositoryCustom {
}

interface OrderRepositoryCustom {
    void persist(Order order);

    Order getOrder(String order_id);

    void update(Order order);
}

class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    JdbcClient jdbcClient;

    public OrderRepositoryCustomImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    @Transactional
    public void persist(Order order) {
        String coupon = null;
        if (order.getCoupons().size() > 0) {
            coupon = order.getCoupons().get(0).getCode();
        }

        String sqlCreateOrder = """
                INSERT INTO
                    orders (id, total, status, client_id, client_email, coupon, created_at)
                VALUES
                    (:id, :total, :status, :client_id, :client_email, :coupon, :created_at)
                    """;

        this.jdbcClient.sql(sqlCreateOrder).param("id", order.getId())
                /* fix this address later */
                .param("total", order.getTotal()).param("status", order.getStatus())
                .param("client_id", order.getClientId()).param("client_email", order.getClientEmail())
                .param("coupon", coupon).param("created_at", order.getOrderDate()).update();

        for (var item : order.getOrderItems()) {
            String sqlCreateOrderItem = """
                    INSERT INTO
                        order_items (id, unity_price, quantity, order_id, product_id)
                    VALUES
                        (:id, :unity_price, :quantity, :order_id, :product_id)
                    """;
            this.jdbcClient.sql(sqlCreateOrderItem).param("id", item.id()).param("unity_price", item.unityPrice())
                    .param("quantity", item.quantity()).param("order_id", order.getId())
                    .param("product_id", item.productId()).update();
        }
    }

    @Override
    public Order getOrder(String order_id) {
        throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
    }

    @Override
    public void update(Order order) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
