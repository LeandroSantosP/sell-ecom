package com.leandrosps.demo_sell_ecom.domain.events;

import com.leandrosps.demo_sell_ecom.infra.mediator.IEvent;

import lombok.Getter;

@Getter
public class ProductStatusUpdated implements IEvent {

	private String order_id;
	private String status;
	private String client_id;;

	public ProductStatusUpdated(String order_id, String status,String client_id) {
		this.order_id = order_id;
		this.status = status;
		this.client_id = client_id;
	}

	@Override
	public String getEventName() {
		return "product-status-updated";
	}

}
