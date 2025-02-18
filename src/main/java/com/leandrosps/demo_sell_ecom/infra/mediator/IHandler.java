package com.leandrosps.demo_sell_ecom.infra.mediator;

public interface IHandler {
	public String eventName();
	public void handle(IEvent event);
}
