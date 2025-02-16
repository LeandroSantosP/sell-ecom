package com.leandrosps.demo_sell_ecom.application;

import java.time.Clock;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.db.ClientRepository;
import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.db.ProductRepository;
import com.leandrosps.demo_sell_ecom.db.dbmodels.ClientDbModel;
import com.leandrosps.demo_sell_ecom.db.dbmodels.OrderDbModel;
import com.leandrosps.demo_sell_ecom.domain.Address;
import com.leandrosps.demo_sell_ecom.domain.Client;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.domain.Order;
import com.leandrosps.demo_sell_ecom.domain.OrderItem;
import com.leandrosps.demo_sell_ecom.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.geteways.AdressGeteWay;
import com.leandrosps.demo_sell_ecom.geteways.MyClock;
import com.leandrosps.demo_sell_ecom.geteways.PaymentGeteWay;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private JdbcClient jdbcClient;
	private OrderRepository orderRepository;
	private ProductRepository productRepository;
	private ClientRepository clientRepository;
	private CouponRepository couponRepository;
	private MyClock clock;
	private PaymentGeteWay paymentGeteWay;

	private AdressGeteWay adressGeteWay;

	public OrderService(JdbcClient jdbcClient, OrderRepository orderRepository, ProductRepository productRepository,
			ClientRepository clientRepository, AdressGeteWay adressGeteWay, CouponRepository couponRepository,
			PaymentGeteWay paymentGeteWay, MyClock clock) {
		this.jdbcClient = jdbcClient;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
		this.adressGeteWay = adressGeteWay;
		this.couponRepository = couponRepository;
		this.paymentGeteWay = paymentGeteWay;
		this.clock = clock;
	}

	public record ItemInputs(String procuct_id, Integer quantity) {
	}

	public String placeOrder(String email, List<ItemInputs> orderItems, String addressCode,
			String couponCode) {
		ClientDbModel clientData = this.clientRepository.findByEmail(email).orElseThrow(() -> new NotFoundEx());
		Client client = new Client(UUID.fromString(clientData.id()), clientData.name(), clientData.email(),
				clientData.city(), clientData.birthday(), clientData.create_at());

		var order = Order.create(client.getId().toString(), client.getEmail(), this.clock);

		for (var item : orderItems) {
			var productData = this.productRepository.findById(item.procuct_id())
					.orElseThrow(() -> new NotFoundEx("Product not found!"));
			order.addItem(productData.price(), item.quantity(), productData.id());
		}

		if (couponCode != (null)) {
			MyCoupon coupon = this.couponRepository.getByCode(couponCode);
			order.addCoupon(coupon);
		}

		Address address = adressGeteWay.getAdress(addressCode);
		order.calcTotal(address); /* calc the total */
		this.orderRepository.persist(order);

		log.info("Order Create With Success!");
		return order.getId();
	}

	public void makePayment(String order_id, String gatewayToken) {
		var order = this.orderRepository.getOrder(order_id);

		var response = this.paymentGeteWay.execut(gatewayToken);

		if (response.status_code() == 400 || response.status().equals("recussed")) {
			/* Failed on payment - Undo everything ?? */
			order.updated_status("RECUSSED");
			this.orderRepository.updated_order_status(order);
			return;
		} else if (response.status_code() == 200 && response.status().equals("accept")) {
			order.updated_status("PAYED");
			for (MyCoupon myCoupon : order.getCoupons()) {
				this.couponRepository.update(myCoupon);
			}
			this.orderRepository.updated_order_status(order);
		}
	}

	public record GetOrderOutput(String clientEmail, String status, long total, List<OrderItem> items) {
	}

	public GetOrderOutput getOrder(String orderId) {
		Order order = this.orderRepository.getOrder(orderId);
		this.jdbcClient.sql("""
				SELECT * FROM orders WHERE id = :id
				""").param("id", orderId).query(OrderDbModel.class);
		throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
	}
}