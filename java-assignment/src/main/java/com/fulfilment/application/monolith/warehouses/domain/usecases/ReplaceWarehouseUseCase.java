package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {
    private static final Logger log = LoggerFactory.getLogger(ReplaceWarehouseUseCase.class);

    @Inject
    ArchiveWarehouseOperation archiveWarehouseOperation;

    @Inject
    CreateWarehouseOperation createWarehouseOperation;

    @Inject
    GetWarehouseOperation getWarehouseOperation;

    @Transactional
    @Override
    public void replace(WarehouseRecord newWarehouse) throws UnknownWarehouseException {
        log.info("Attempting to replace warehouse with new data: {}", newWarehouse);
        if (newWarehouse == null) {
            log.error("Warehouse data must be provided for replacement.");
            throw new BusinessLogicException("Warehouse data should be provided");
        }
        if (newWarehouse.id() != null) {
            log.error("New warehouse data should not contain an identifier, but found: {}", newWarehouse.id());
            throw new BusinessLogicException("Warehouse data should not cointain identifier");
        }
        WarehouseRecord toArchive = getWarehouseOperation.getActiveByUnitId(newWarehouse.businessUnitCode());
        log.info("Found warehouse to archive: {}", toArchive);
        validateStock(newWarehouse, toArchive);
        archiveWarehouseOperation.archive(toArchive);
        createWarehouseOperation.create(newWarehouse);
        log.info("Warehouse replaced successfully. New warehouse ID: {}", newWarehouse.id());
    }

    private void validateStock(WarehouseRecord newWarehouse, WarehouseRecord toArchive) {
        log.info("Validating stock for warehouse replacement. New stock: {}, old stock: {}", newWarehouse.stock(), toArchive.stock());
        if (!Objects.equals(newWarehouse.stock(), toArchive.stock())) {
            log.error("Stock does not match. New stock: {}, old stock: {}", newWarehouse.stock(), toArchive.stock());
            throw new BusinessLogicException("Stock does not macth " + newWarehouse.stock() + " in new vs " + toArchive.stock() + " in archived"
            );
        }
    }
}
