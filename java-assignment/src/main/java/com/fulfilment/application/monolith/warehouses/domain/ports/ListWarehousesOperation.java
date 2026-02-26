package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;

import java.util.Collection;

public interface ListWarehousesOperation {
    Collection<WarehouseRecord> list(boolean includeArchived);
}
