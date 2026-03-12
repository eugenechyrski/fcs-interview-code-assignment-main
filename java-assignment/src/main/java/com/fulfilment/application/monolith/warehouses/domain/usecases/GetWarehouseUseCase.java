package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class GetWarehouseUseCase implements GetWarehouseOperation {

    private static final Logger log = LoggerFactory.getLogger(GetWarehouseUseCase.class);

    @Inject
    protected WarehouseStore warehouseStore;

    @Override
    public WarehouseRecord getActiveById(String id) {
        log.info("Getting active warehouse by ID: {}", id);
        return warehouseStore.findById(Long.parseLong(id));
    }

    @Override
    public WarehouseRecord getActiveByUnitId(String unitId) {
        log.info("Getting active warehouse by Unit ID: {}", unitId);
        return warehouseStore.findByBusinessUnitCode(unitId);
    }
}
