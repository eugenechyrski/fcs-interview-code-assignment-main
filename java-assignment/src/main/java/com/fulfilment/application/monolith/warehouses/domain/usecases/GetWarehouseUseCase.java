package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class GetWarehouseUseCase implements GetWarehouseOperation {

    @Inject
    protected WarehouseStore warehouseStore;

    @Override
    public WarehouseRecord getActiveById(String id) {
        return warehouseStore.findById(Long.parseLong(id));
    }

    @Override
    public WarehouseRecord getActiveByUnitId(String unitId) {
        return warehouseStore.findByBusinessUnitCode(unitId);
    }
}

