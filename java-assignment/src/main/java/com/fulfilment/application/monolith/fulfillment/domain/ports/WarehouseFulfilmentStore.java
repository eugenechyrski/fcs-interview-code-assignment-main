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
    boolean existsByProductStoreWarehouse(String product, String store, String warehouse);

    /**
     * Count how many warehouses fulfill a given product in a store.
     */
    long countByProductAndStore(String product, String store);

    /**
     * Count distinct warehouses serving a store.
     */
    long countDistinctWarehousesByStore(String store);

    /**
     * Count how many products are stored in a given warehouse.
     */
    long countByWarehouse(String warehouse);
}
