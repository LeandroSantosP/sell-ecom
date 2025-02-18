package com.leandrosps.demo_sell_ecom.domain.events;

import com.leandrosps.demo_sell_ecom.infra.mediator.IEvent;
import lombok.Getter;

@Getter
public class PaymentOrderRefussedEvent implements IEvent {
	private String order_id;
	private String status;
	private String content;

	public PaymentOrderRefussedEvent(String order_id, String status, String content) {
		this.order_id = order_id;
		this.status = status;
		this.content = content;
	}

	@Override
	public String getEventName() {
		return "payment-order-refussed";
	}

}
