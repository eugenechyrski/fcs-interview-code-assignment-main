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

import java.util.Objects;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

    @Inject
    ArchiveWarehouseOperation archiveWarehouseOperation;

    @Inject
    CreateWarehouseOperation createWarehouseOperation;

    @Inject
    GetWarehouseOperation getWarehouseOperation;

    @Transactional
    @Override
    public void replace(WarehouseRecord newWarehouse) throws UnknownWarehouseException {
        if (newWarehouse == null) {
            throw new BusinessLogicException("Warehouse data should be provided");
        }
        if (newWarehouse.id() != null) {
            throw new BusinessLogicException("Warehouse data should not cointain identifier");
        }
        WarehouseRecord toArchive = getWarehouseOperation.getActiveByUnitId(newWarehouse.businessUnitCode());
        validateStock(newWarehouse, toArchive);
        archiveWarehouseOperation.archive(toArchive);

        createWarehouseOperation.create(newWarehouse);
    }

    private void validateStock(WarehouseRecord newWarehouse, WarehouseRecord toArchive) {
        if (!Objects.equals(newWarehouse.stock(), toArchive.stock())) {
            throw new BusinessLogicException("Stock does not macth " + newWarehouse.stock() + " in new vs " + toArchive.stock() + " in archived"
            );
        }
    }
}
