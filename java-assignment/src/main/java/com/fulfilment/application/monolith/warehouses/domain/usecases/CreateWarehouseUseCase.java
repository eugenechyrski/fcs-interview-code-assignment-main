package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseEventPublisher;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {
    @Inject
    WarehouseEventPublisher eventPublisher;

    @Inject
    LocationResolver locationResolver;
    @Inject
    ListWarehousesUseCase listWarehousesUseCase;
    @Inject
    WarehouseStore warehouseStore;

    @Transactional

    @Override
    public void create(WarehouseRecord warehouse) {
        if (warehouse == null) {
            throw new BusinessLogicException("Warehouse data must be provided");
        }
        Location location = locationResolver.resolveByIdentifierAndLock(warehouse.location());
        try {
            validateWarehouseLimit(location);
            validateCapacity(location, warehouse);
            validateStock(warehouse);
            warehouseStore.create(warehouse);
        } finally {
            eventPublisher.warehouseCreated(location.identification());
        }

    }

    private void validateWarehouseLimit(Location location) {
        List<WarehouseRecord> records = warehouseStore.findByLocation(location.identification());

        if (records.size() >= location.maxNumberOfWarehouses()) {
            throw new BusinessLogicException("Maximum number of warehouses at location reached"
            );
        }
    }

    private void validateCapacity(Location location, WarehouseRecord warehouse) {
        if (warehouse.capacity() > location.maxCapacity()) {
            throw new BusinessLogicException("Warehouse capacity exceeds location limit"
            );
        }
    }

    private void validateStock(WarehouseRecord warehouse) {
        if (warehouse.stock() > warehouse.capacity()) {
            throw new BusinessLogicException("Stock exceeds warehouse capacity"
            );
        }
    }
}
