package com.fulfilment.application.monolith.fulfillment.domain.ports;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilment;

public interface WarehouseFulfilmentStore {
    /**
     * Persist a new WarehouseFulfilment entity.
     */
    void save(WarehouseFulfilment wf);

    /**
     * Check if a given product-store-warehouse combination already exists.
     */
    boolean existsByProductStoreWarehouse(long product, long store, String warehouse);

    /**
     * Count how many warehouses fulfill a given product in a store.
     */
    long countByProductAndStore(long product, long store);

    /**
     * Count distinct warehouses serving a store.
     */
    long countDistinctWarehousesByStore(long store);

    /**
     * Count how many products are stored in a given warehouse.
     */
    long countByWarehouse(String warehouse);
}
