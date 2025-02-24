package com.leandrosps.demo_sell_ecom.domain.events;

import com.leandrosps.demo_sell_ecom.infra.mediator.IEvent;

import lombok.Getter;

@Getter
public class PayemntAccept implements IEvent {

	private String order_id;

	public PayemntAccept(String order_id) {
		this.order_id = order_id;
	}

	@Override
	public String getEventName() {
		return "payemnt-accept";
	}

}
