package com.leandrosps.demo_sell_ecom.application;

import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.db.ClientDbModel;
import com.leandrosps.demo_sell_ecom.db.OrderDbModel;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.db.ProductDbModel;
import com.leandrosps.demo_sell_ecom.domain.Client;
import com.leandrosps.demo_sell_ecom.domain.Order;
import com.leandrosps.demo_sell_ecom.domain.OrderItem;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

@Service
public class OrderService {

	private JdbcClient jdbcClient;
	private OrderRepository orderRepository;

	public OrderService(JdbcClient jdbcClient, OrderRepository orderRepository) {
		this.jdbcClient = jdbcClient;
		this.orderRepository = orderRepository;
	}

	public record ItemInputs(String procuct_id, Integer quantity) {
	}

	public record PlaceOrderinput() {
	}

	public String placeOrder(String email, List<ItemInputs> orderItems) {
		/* Create an repository? who knows */
		var clientData = this.jdbcClient.sql(/* language=sql */"""
					SELECT * FROM clients WHERE email = :email
				""")
				.param("email", email)
				.query(ClientDbModel.class)
				.optional()
				.orElseThrow(() -> new NotFoundEx());

		Client client = new Client(UUID.fromString(clientData.id()),
				clientData.name(), clientData.email(),
				clientData.city(), clientData.birthday(), clientData.create_at());

		var order = Order.create(client.getEmail(), null);

		for (var item : orderItems) {
			String getProductSql = """
					SELECT * FROM products WHERE id = :id
					""";
			var productData = this.jdbcClient
					.sql(getProductSql)
					.param("id", item.procuct_id())
					.query(ProductDbModel.class)
					.optional().orElseThrow(() -> new NotFoundEx());
			order.addItem(productData.id(), productData.price(), item.quantity());
		}

		this.orderRepository.save(order);
		
		return order.getId();
	}

	public record GetOrderOutput(String clientEmail, String status, long total, List<OrderItem> items) {
	}

	public GetOrderOutput getOrder(String orderId) {

		var order = this.orderRepository.getOrder(orderId);

		this.jdbcClient.sql("""
					SELECT * FROM orders WHERE id = :id
				""")
				.param("id", orderId)
				.query(OrderDbModel.class);
		throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
	}
}
