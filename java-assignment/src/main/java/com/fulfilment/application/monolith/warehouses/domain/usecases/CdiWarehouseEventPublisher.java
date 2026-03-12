package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseEventPublisher;
import com.fulfilment.application.monolith.warehouses.domain.usecases.events.WarehouseCreationEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CdiWarehouseEventPublisher implements WarehouseEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CdiWarehouseEventPublisher.class);

    @Inject
    Event<WarehouseCreationEvent> event;

    @Override
    public void warehouseCreated(String locationId) {
        log.info("Firing WarehouseCreationEvent for locationId: {}", locationId);
        event.fire(new WarehouseCreationEvent(locationId));
    }
}
