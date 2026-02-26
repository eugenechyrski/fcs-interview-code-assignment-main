package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseEventPublisher;
import com.fulfilment.application.monolith.warehouses.domain.usecases.events.WarehouseCreationEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class CdiWarehouseEventPublisher implements WarehouseEventPublisher {
    @Inject
    Event<WarehouseCreationEvent> event;

    @Override
    public void warehouseCreated(String locationId) {
        event.fire(new WarehouseCreationEvent(locationId));
    }
}
