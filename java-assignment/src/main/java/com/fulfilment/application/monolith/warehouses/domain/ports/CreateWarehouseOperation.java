package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;

public interface CreateWarehouseOperation {
    void create(WarehouseRecord warehouse);
}
