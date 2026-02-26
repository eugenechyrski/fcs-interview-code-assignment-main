package com.fulfilment.application.monolith.warehouses.domain.ports;

public interface WarehouseEventPublisher {
    void warehouseCreated(String locationId);
}
