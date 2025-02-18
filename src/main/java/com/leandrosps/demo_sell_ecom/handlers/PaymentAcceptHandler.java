package com.leandrosps.demo_sell_ecom.handlers;

import org.springframework.stereotype.Component;
import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.domain.MyCoupon;
import com.leandrosps.demo_sell_ecom.domain.events.PaymentOrderAcceptEvent;
import com.leandrosps.demo_sell_ecom.domain.events.ProductStatusUpdated;
import com.leandrosps.demo_sell_ecom.infra.mediator.IEvent;
import com.leandrosps.demo_sell_ecom.infra.mediator.IHandler;
import com.leandrosps.demo_sell_ecom.infra.mediator.Mediator;

@Component
public class PaymentAcceptHandler implements IHandler {

	@Override
	public String eventName() {
		return "payment-order-accepted";
	}

	private OrderRepository orderRepository;
	private CouponRepository couponRepository;
	private Mediator mediator;

	public PaymentAcceptHandler(OrderRepository orderRepository, CouponRepository couponRepository, Mediator mediator) {
		this.orderRepository = orderRepository;
		this.couponRepository = couponRepository;
		this.mediator = mediator;
	}

	@Override
	public void handle(IEvent event) {
		if (!(event instanceof PaymentOrderAcceptEvent)) {
			return;
		}
		var placeOrderEvent = (PaymentOrderAcceptEvent) event;
		var order = this.orderRepository.getOrder(placeOrderEvent.getOrder_id());

		order.updated_status("PAID");

		this.orderRepository.updated_order_status(order);

		mediator.publisher(new ProductStatusUpdated(order.getId(), placeOrderEvent.getStatus(), order.getClientId()));
	}

}
