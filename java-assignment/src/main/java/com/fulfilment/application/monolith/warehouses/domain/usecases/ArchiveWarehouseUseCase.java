package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;


@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

    @Inject
    GetWarehouseOperation getWarehouseOperation;

    @Inject
    WarehouseStore warehouseStore;

    @Transactional
    @Override
    public void archive(WarehouseRecord warehouse) throws UnknownWarehouseException {
        if (warehouse == null) {
            throw new BusinessLogicException("Warehouse data must be provided");
        }
        WarehouseRecord warehouseRecord = getWarehouseOperation.getActiveById(warehouse.id());
        var archivedWarehouse = new WarehouseRecord(
                warehouseRecord.id(),
                warehouseRecord.businessUnitCode(),
                warehouseRecord.location(),
                warehouseRecord.capacity(),
                warehouseRecord.stock(),
                warehouseRecord.createdAt(),
                LocalDateTime.now()
        );

        warehouseStore.update(archivedWarehouse);
    }

}
