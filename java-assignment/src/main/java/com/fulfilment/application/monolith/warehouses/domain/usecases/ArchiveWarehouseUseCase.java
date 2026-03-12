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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

    private static final Logger log = LoggerFactory.getLogger(ArchiveWarehouseUseCase.class);

    @Inject
    GetWarehouseOperation getWarehouseOperation;

    @Inject
    WarehouseStore warehouseStore;

    @Transactional
    @Override
    public void archive(WarehouseRecord warehouse) throws UnknownWarehouseException {
        log.info("Attempting to archive warehouse with ID: {}", warehouse != null ? warehouse.id() : "null");
        if (warehouse == null) {
            log.error("Warehouse data must be provided for archiving.");
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

        log.info("Archiving warehouse: {}", archivedWarehouse.id());
        warehouseStore.update(archivedWarehouse);
        log.info("Warehouse with ID {} archived successfully.", archivedWarehouse.id());
    }

}
