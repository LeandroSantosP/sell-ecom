package com.leandrosps.demo_sell_ecom.infra.mediator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

public interface Mediator {
	public void publisher(IEvent event);

	void register(IHandler handler);
}

@Component
class MediatorInMemo implements Mediator {
	List<IHandler> handlers = new ArrayList<>();

	@Override
	public void publisher(IEvent event) {
		for (IHandler handler : this.handlers) {
			if (event.getEventName().equals(handler.eventName())) {
				handler.handle(event);
			}
		}
	}

	@Override
	public void register(IHandler handler) {
		this.handlers.add(handler);
	}
};
