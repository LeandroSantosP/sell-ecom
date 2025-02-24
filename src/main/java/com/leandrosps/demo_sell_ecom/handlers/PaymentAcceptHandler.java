package com.leandrosps.demo_sell_ecom.handlers;

import org.springframework.stereotype.Component;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.domain.events.PaymentOrderAcceptEvent;
import com.leandrosps.demo_sell_ecom.domain.events.ProductStatusUpdated;
import com.leandrosps.demo_sell_ecom.geteways.Mail;
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
	private Mediator mediator;
	private Mail mail;

	public PaymentAcceptHandler(OrderRepository orderRepository, Mediator mediator, Mail mail) {
		this.orderRepository = orderRepository;
		this.mediator = mediator;
		this.mail = mail;
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

		this.mail.send(order.getClientEmail(),"Order Payment",  String.format("""
			Your order payment has been approved!
				Order id: %s
				Order status: %s
			""", order.getId(), order.getId(), order.getStatus()));

		mediator.publisher(new ProductStatusUpdated(order.getId(), placeOrderEvent.getStatus(), order.getClientId()));
	}

}
