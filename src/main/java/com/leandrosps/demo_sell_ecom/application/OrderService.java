package com.leandrosps.demo_sell_ecom.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.db.ClientRepository;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.db.ProductRepository;
import com.leandrosps.demo_sell_ecom.db.dbmodels.ClientDbModel;
import com.leandrosps.demo_sell_ecom.db.dbmodels.OrderDbModel;
import com.leandrosps.demo_sell_ecom.domain.Client;
import com.leandrosps.demo_sell_ecom.domain.Order;
import com.leandrosps.demo_sell_ecom.domain.OrderItem;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;

@Service
public class OrderService {

	private JdbcClient jdbcClient;
	private OrderRepository orderRepository;
	private ProductRepository productRepository;
	private ClientRepository clientRepository;

	public OrderService(JdbcClient jdbcClient, OrderRepository orderRepository, ProductRepository productRepository,
			ClientRepository clientRepository) {
		this.jdbcClient = jdbcClient;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
	}

	public record ItemInputs(String procuct_id, Integer quantity) {
	}

	public record PlaceOrderinput() {
	}

	public String placeOrder(String email, List<ItemInputs> orderItems, String gatewayToken) {
		/* Create an repository? who knows */
		ClientDbModel clientData = this.clientRepository.findByEmail(email).orElseThrow(() -> new NotFoundEx());

		Client client = new Client(UUID.fromString(clientData.id()), clientData.name(), clientData.email(),
				clientData.city(), clientData.birthday(), clientData.create_at());

		var order = Order.create(client.getId().toString(), client.getEmail(), null);

		for (var item : orderItems) {
			var productData = this.productRepository.findById(item.procuct_id())
					.orElseThrow(() -> new NotFoundEx("Product not found!"));
			order.addItem(productData.price(), item.quantity(), productData.id());
		}

		this.orderRepository.persist(order);
		var result = this.orderRepository.findById(order.getId());
		System.out.println(result.get());
		return order.getId();
	}

	public record GetOrderOutput(String clientEmail, String status, long total, List<OrderItem> items) {
	}

	public GetOrderOutput getOrder(String orderId) {

		// var order = this.orderRepository.getOrder(orderId);

		this.jdbcClient.sql("""
				SELECT * FROM orders WHERE id = :id
				""").param("id", orderId).query(OrderDbModel.class);
		throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
	}
}
