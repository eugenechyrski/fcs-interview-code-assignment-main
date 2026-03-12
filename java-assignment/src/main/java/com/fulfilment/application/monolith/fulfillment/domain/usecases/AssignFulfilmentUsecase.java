package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilment;
import com.fulfilment.application.monolith.fulfillment.domain.ports.AssignFulfilmentOperation;
import com.fulfilment.application.monolith.fulfillment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@ApplicationScoped
public class AssignFulfilmentUsecase implements AssignFulfilmentOperation {

    private static final Logger log = LoggerFactory.getLogger(AssignFulfilmentUsecase.class);

    @Inject
    protected WarehouseFulfilmentStore warehouseFulfilmentStore;

    @Transactional
    @Override
    public void assignFulfilment(String product, String store, String warehouse) {
        log.info("Assigning product '{}' to store '{}' via warehouse '{}'", product, store, warehouse);

        // Check if this warehouse already fulfills this product-store
        boolean exists = warehouseFulfilmentStore.existsByProductStoreWarehouse(product, store, warehouse);
        if (exists) {
            log.warn("This warehouse '{}' already fulfills product '{}' for store '{}'", warehouse, product, store);
            return;
        }

        // Constraint 1: Each Product max 2 warehouses per store
        long productWarehouseCount = warehouseFulfilmentStore.countByProductAndStore(product, store);
        if (productWarehouseCount >= 2) {
            throw new BusinessLogicException(
                    "Cannot assign product '" + product + "' to more than 2 warehouses for store '" + store + "'"
            );
        }

        // Constraint 2: Each store max 3 warehouses
        long storeWarehouseCount = warehouseFulfilmentStore.countDistinctWarehousesByStore(store);
        if (storeWarehouseCount >= 3) {
            throw new BusinessLogicException(
                    "Cannot assign more than 3 warehouses for store '" + store + "'"
            );
        }

        // Constraint 3: Each warehouse max 5 products
        long warehouseProductCount = warehouseFulfilmentStore.countByWarehouse(warehouse);
        if (warehouseProductCount >= 5) {
            throw new BusinessLogicException(
                    "Warehouse '" + warehouse + "' cannot store more than 5 different products"
            );
        }

        // If all constraints pass, create the fulfilment entry
        WarehouseFulfilment wf = new WarehouseFulfilment();
        wf.id = null;
        wf.product = product;
        wf.store = store;
        wf.warehouse = warehouse;
        wf.createdAt = LocalDateTime.now();
        wf.updatedAt = LocalDateTime.now();

        warehouseFulfilmentStore.save(wf);
        log.info("Successfully assigned product '{}' to store '{}' via warehouse '{}'", product, store, warehouse);
    }
}