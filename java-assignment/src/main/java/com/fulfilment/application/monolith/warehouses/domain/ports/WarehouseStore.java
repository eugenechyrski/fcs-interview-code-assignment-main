package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;

import java.util.List;

public interface WarehouseStore {
    /**
     *
     * @return all awailable warehouses. Probably not a good idea  - some pagination necessary here
     */
    List<WarehouseRecord> getAll();

    /**
     * Create warehouse
     *
     * @param warehouse to be created
     */
    void create(WarehouseRecord warehouse);

    /**
     * Update warehouse
     *
     * @param warehouse to be updated
     */
    void update(WarehouseRecord warehouse);

    /**
     *
     * @param warehouse to be deleted
     */
    void remove(WarehouseRecord warehouse);

    /**
     *
     * @param buCode business unit code
     * @return warehouse within provided business unit
     */
    WarehouseRecord findByBusinessUnitCode(String buCode);

    /**
     * Return record by primary key
     *
     * @param id primary key
     * @return warehouse
     */
    WarehouseRecord findById(long id);

    /**
     * returns warehouses by location
     *
     * @param location
     * @return
     */
    List<WarehouseRecord> findByLocation(String location);
}
