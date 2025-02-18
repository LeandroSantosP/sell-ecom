package com.leandrosps.demo_sell_ecom.handlers;

import org.springframework.stereotype.Component;
import com.leandrosps.demo_sell_ecom.db.CouponRepository;
import com.leandrosps.demo_sell_ecom.db.OrderRepository;
import com.leandrosps.demo_sell_ecom.domain.events.ProductStatusUpdated;
import com.leandrosps.demo_sell_ecom.infra.mediator.IEvent;
import com.leandrosps.demo_sell_ecom.infra.mediator.IHandler;
import com.leandrosps.demo_sell_ecom.infra.mediator.Mediator;

@Component
public class ProductStatusUpdatedHandler implements IHandler {

	@Override
	public String eventName() {
		return "product-status-updated";
	}

	private OrderRepository orderRepository;
	private CouponRepository couponRepository;
	private Mediator mediator;

	public ProductStatusUpdatedHandler(OrderRepository orderRepository, CouponRepository couponRepository, Mediator mediator) {
		this.orderRepository = orderRepository;
		this.couponRepository = couponRepository;
		this.mediator = mediator;
	}

	@Override
	public void handle(IEvent event) {
		if (!(event instanceof ProductStatusUpdated)) {
			return;
		}
		var pSUEvent = (ProductStatusUpdated) event;
		/* Send  An Email to the client*/

		
	}

}
