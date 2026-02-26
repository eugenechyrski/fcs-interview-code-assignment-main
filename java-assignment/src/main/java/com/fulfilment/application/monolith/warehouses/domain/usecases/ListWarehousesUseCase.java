package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.ListWarehousesOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.stream.Collectors;


@ApplicationScoped
public class ListWarehousesUseCase implements ListWarehousesOperation {


    @Inject
    WarehouseStore warehouseStore;

    @Override
    public Collection<WarehouseRecord> list(boolean includeArchived) {
        //TODO: refactor to filter in db
        return warehouseStore
                .getAll()
                .stream()
                .filter(warehouse -> warehouse.archivedAt() == null || includeArchived)
                .collect(Collectors.toList());
    }
}
