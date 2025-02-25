package com.leandrosps.demo_sell_ecom.handlers;

import org.springframework.stereotype.Component;

import com.leandrosps.demo_sell_ecom.domain.events.PaymentOrderRefussedEvent;
import com.leandrosps.demo_sell_ecom.domain.events.ProductStatusUpdated;
import com.leandrosps.demo_sell_ecom.infra.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.infra.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.infra.geteways.Mail;
import com.leandrosps.demo_sell_ecom.infra.mediator.IEvent;
import com.leandrosps.demo_sell_ecom.infra.mediator.IHandler;
import com.leandrosps.demo_sell_ecom.infra.mediator.Mediator;

@Component
public class PaymentRecussedHandler implements IHandler {

	@Override
	public String eventName() {
		return "payment-order-refussed";
	}

	private OrderRepository orderRepository;
	private CouponRepository couponRepository;
	private Mediator mediator;
	private Mail mail;

	public PaymentRecussedHandler(OrderRepository orderRepository, Mediator mediator,
			CouponRepository couponRepository, Mail mail) {
		this.orderRepository = orderRepository;
		this.mediator = mediator;
		this.couponRepository = couponRepository;
		this.mail = mail;
	}

	@Override
	public void handle(IEvent event) {
		if (!(event instanceof PaymentOrderRefussedEvent)) {
			return;
		}
		var placeOrderEvent = (PaymentOrderRefussedEvent) event;
		var order = this.orderRepository.getOrder(placeOrderEvent.getOrder_id());

		order.getCoupons().forEach(c -> {
			var coupon = this.couponRepository.getByCode(c.getCode());
			coupon.decreseUsage();
			this.couponRepository.update(coupon);
		});

		order.updated_status("RECUSSED");
		this.orderRepository.updated_order_status(order);
		this.mail.send(order.getClientEmail(),"Order Payment",  String.format("""
			Your order payment has been recussed!
				Order id: %s
				Order status: %s
			""", order.getId(), order.getStatus()));
			
		mediator.publisher(new ProductStatusUpdated(order.getId(), placeOrderEvent.getStatus(), order.getClientId()));
	}
}
