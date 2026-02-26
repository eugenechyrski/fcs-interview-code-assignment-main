package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;

public interface GetWarehouseOperation {
    /**
     *
     * @param buCode business unit code
     * @return warehouse information
     */
    WarehouseRecord getActiveById(String buCode);

    WarehouseRecord getActiveByUnitId(String unitId);
}
