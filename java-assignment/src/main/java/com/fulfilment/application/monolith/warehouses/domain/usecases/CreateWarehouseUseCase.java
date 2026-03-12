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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

    private static final Logger log = LoggerFactory.getLogger(CreateWarehouseUseCase.class);

    @Inject
    WarehouseEventPublisher eventPublisher;

    @Inject
    LocationResolver locationResolver;

    @Inject
    WarehouseStore warehouseStore;

    @Transactional
    @Override
    public void create(WarehouseRecord warehouse) {
        log.info("Attempting to create warehouse: {}", warehouse);
        if (warehouse == null) {
            log.error("Warehouse data must be provided for creation.");
            throw new BusinessLogicException("Warehouse data must be provided");
        }
        Location location = locationResolver.resolveByIdentifierAndLock(warehouse.location());
        try {
            validateWarehouseLimit(location);
            validateCapacity(location, warehouse);
            validateStock(warehouse);
            log.info("Creating warehouse: {}", warehouse);
            warehouseStore.create(warehouse);
            log.info("Warehouse created successfully: {}", warehouse.id());
        } finally {
            eventPublisher.warehouseCreated(location.identification());
        }

    }

    private void validateWarehouseLimit(Location location) {
        log.info("Validating warehouse limit for location: {}", location.identification());
        List<WarehouseRecord> records = warehouseStore.findByLocation(location.identification());

        if (records.size() >= location.maxNumberOfWarehouses()) {
            log.error("Maximum number of warehouses reached for location: {}", location.identification());
            throw new BusinessLogicException("Maximum number of warehouses at location reached"
            );
        }
    }

    private void validateCapacity(Location location, WarehouseRecord warehouse) {
        log.info("Validating capacity for warehouse at location: {}", location.identification());
        if (warehouse.capacity() > location.maxCapacity()) {
            log.error("Warehouse capacity {} exceeds location limit {} for location: {}", warehouse.capacity(), location.maxCapacity(), location.identification());
            throw new BusinessLogicException("Warehouse capacity exceeds location limit"
            );
        }
    }

    private void validateStock(WarehouseRecord warehouse) {
        log.info("Validating stock for warehouse: {}", warehouse.id());
        if (warehouse.stock() > warehouse.capacity()) {
            log.error("Stock {} exceeds warehouse capacity {} for warehouse: {}", warehouse.stock(), warehouse.capacity(), warehouse.id());
            throw new BusinessLogicException("Stock exceeds warehouse capacity"
            );
        }
    }
}
