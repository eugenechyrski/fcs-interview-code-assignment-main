package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilment;
import com.fulfilment.application.monolith.fulfillment.domain.ports.AssignFulfilmentOperation;
import com.fulfilment.application.monolith.fulfillment.domain.ports.WarehouseFulfilmentStore;
import com.fulfilment.application.monolith.products.ProductRepository;
import com.fulfilment.application.monolith.stores.Store;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import com.fulfilment.application.monolith.warehouses.exception.InvalidWarehouseIdException;
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

    @Inject
    protected GetWarehouseOperation getWarehouseOperation;

    @Inject
    protected ProductRepository productRepository;

    @Transactional
    @Override
    public void assignFulfilment(long productId, long storeId, String warehouse) {
        log.info("Assigning product '{}' to store '{}' via warehouse '{}'", productId, storeId, warehouse);

        if (getWarehouseOperation.getActiveByUnitId(warehouse) == null) {
            throw new InvalidWarehouseIdException();
        }
        if (Store.findById(storeId) == null) {
             throw new BusinessLogicException("Store not found");
        }
        if (productRepository.findById(productId) == null) {
            throw new BusinessLogicException("Product not found");
        }

        // Check if this warehouse already fulfills this product-store
        boolean exists = warehouseFulfilmentStore.existsByProductStoreWarehouse(productId, storeId, warehouse);
        if (exists) {
            log.warn("This warehouse '{}' already fulfills product '{}' for store '{}'", warehouse, productId, storeId);
            return;
        }

        // Constraint 1: Each Product max 2 warehouses per store
        long productWarehouseCount = warehouseFulfilmentStore.countByProductAndStore(productId, storeId);
        if (productWarehouseCount >= 2) {
            throw new BusinessLogicException(
                    "Cannot assign product '" + productId + "' to more than 2 warehouses for store '" + storeId + "'"
            );
        }

        // Constraint 2: Each store max 3 warehouses
        long storeWarehouseCount = warehouseFulfilmentStore.countDistinctWarehousesByStore(storeId);
        if (storeWarehouseCount >= 3) {
            throw new BusinessLogicException(
                    "Cannot assign more than 3 warehouses for store '" + storeId + "'"
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
        wf.productId = productId;
        wf.storeId = storeId;
        wf.warehouse = warehouse;
        wf.createdAt = LocalDateTime.now();
        wf.updatedAt = LocalDateTime.now();

        warehouseFulfilmentStore.save(wf);
        log.info("Successfully assigned product '{}' to store '{}' via warehouse '{}'", productId, storeId, warehouse);
    }
}