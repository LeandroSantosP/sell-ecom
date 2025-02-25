package com.leandrosps.demo_sell_ecom.application.services;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.domain.Address;
import com.leandrosps.demo_sell_ecom.domain.Client;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.domain.Order;
import com.leandrosps.demo_sell_ecom.domain.OrderItem;
import com.leandrosps.demo_sell_ecom.domain.events.PaymentOrderAcceptEvent;
import com.leandrosps.demo_sell_ecom.domain.events.PaymentOrderRefussedEvent;
import com.leandrosps.demo_sell_ecom.infra.db.ClientRepository;
import com.leandrosps.demo_sell_ecom.infra.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.infra.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.infra.db.ProductRepository;
import com.leandrosps.demo_sell_ecom.infra.db.dbmodels.ClientDbModel;
import com.leandrosps.demo_sell_ecom.infra.errors.NotFoundEx;
import com.leandrosps.demo_sell_ecom.infra.geteways.AdressGeteWay;
import com.leandrosps.demo_sell_ecom.infra.geteways.MyClock;
import com.leandrosps.demo_sell_ecom.infra.geteways.PaymentGeteWay;
import com.leandrosps.demo_sell_ecom.infra.mediator.Mediator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private OrderRepository orderRepository;
	private ProductRepository productRepository;
	private ClientRepository clientRepository;
	private CouponRepository couponRepository;
	private MyClock clock;
	private PaymentGeteWay paymentGeteWay;
	private AdressGeteWay adressGeteWay;
	private Mediator mediator;;

	public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
			ClientRepository clientRepository, AdressGeteWay adressGeteWay, CouponRepository couponRepository,
			PaymentGeteWay paymentGeteWay, MyClock clock, Mediator mediator) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
		this.adressGeteWay = adressGeteWay;
		this.couponRepository = couponRepository;
		this.paymentGeteWay = paymentGeteWay;
		this.clock = clock;
		this.mediator = mediator;
	}

	public record ItemInputs(String procuct_id, Integer quantity) {
	}

	public String placeOrder(String email, List<ItemInputs> orderItems, String addressCode, String couponCode) {
		System.out.println(this.clientRepository.count());
		ClientDbModel clientData = this.clientRepository.findByFkEmail(email).orElseThrow(() -> new NotFoundEx());
		Client client = new Client(UUID.fromString(clientData.id()), clientData.name(), clientData.fkEmail(),
				clientData.city(), clientData.birthday(), clientData.create_at());

		var order = Order.create(client.getId().toString(), client.getEmail(), this.clock);

		for (var item : orderItems) {
			var productData = this.productRepository.findById(item.procuct_id())
					.orElseThrow(() -> new NotFoundEx("Product not found!"));
			order.addItem(productData.price(), item.quantity(), productData.id());
		}

		if (couponCode != null) {
			MyCoupon coupon = this.couponRepository.getByCode(couponCode);
			order.addCoupon(coupon);
		}

		Address address = adressGeteWay.getAdress(addressCode);
		order.calcTotal(address); /* calc the total */

		order.getCoupons().forEach(coupon -> {
			this.couponRepository.update(coupon);
		});

		this.orderRepository.persist(order);
		log.info("Order Create With Success!");
		return order.getId();
	}

	public void makePayment(String order_id, String gatewayToken) {

		var order = this.orderRepository.getOrder(order_id);

		var response = this.paymentGeteWay.execut(gatewayToken);

		if (response.status_code() == 400 || response.status().equals("recussed")) {
			this.mediator
					.publisher(new PaymentOrderRefussedEvent(order.getId(), response.status(), response.content()));
			return;
		} else if (response.status_code() == 200 && response.status().equals("accept")) {
			this.mediator.publisher(new PaymentOrderAcceptEvent(order.getId(), response.status(), response.content()));
		}
	}

	public void cancelOrder(String order_id) {
		var order = this.orderRepository.getOrder(order_id);
		order.cancel();
		this.orderRepository.update(order);
	}

	public record GetOrderOutput(String order_id, String clientEmail, String status, long total,
			List<OrderItem> items) {
	}

	public GetOrderOutput getOrder(String orderId) {
		Order order = this.orderRepository.getOrder(orderId);
		return new GetOrderOutput(order.getId(), order.getClientEmail(), order.getStatus(), order.getTotal(),
				order.getOrderItems());
	}

}