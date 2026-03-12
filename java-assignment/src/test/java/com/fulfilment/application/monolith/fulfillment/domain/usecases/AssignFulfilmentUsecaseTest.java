package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilment;
import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilmentRepository;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AssignFulfilmentUsecaseTest {

    @Inject
    AssignFulfilmentUsecase usecase;

    @Inject
    WarehouseFulfilmentRepository repository;

    @BeforeEach
    @Transactional
    void cleanup() {
        repository.deleteAll();
    }

    private WarehouseFulfilment createFulfilment(long productId, long storeId, String warehouse) {
        WarehouseFulfilment wf = new WarehouseFulfilment();
        wf.productId = productId;
        wf.storeId = storeId;
        wf.warehouse = warehouse;
        wf.createdAt = LocalDateTime.now();
        wf.updatedAt = LocalDateTime.now();
        repository.persist(wf);
        return wf;
    }

    @Test
    @Transactional
    void testAssignFulfilmentSuccess() {
        usecase.assignFulfilment(1, 1, "MWH.001");

        boolean exists = repository.existsByProductStoreWarehouse(1, 1, "MWH.001");
        assertTrue(exists);
    }

    @Test
    @Transactional
    void testAssignDuplicateFulfilment() {
        createFulfilment(1, 1, "MWH.001");

        // Should not throw, just log and skip
        usecase.assignFulfilment(1, 1, "MWH.001");

        long count = repository.countByProductAndStore(1, 1);
        assertEquals(1, count);
    }

    @Test
    @Transactional
    void testProductWarehouseConstraintExceeded() {
        createFulfilment(1, 1, "MWH.001");
        createFulfilment(1, 1, "MWH.012");

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                usecase.assignFulfilment(1, 1, "MWH.023")
        );

        assertTrue(ex.getMessage().contains("Cannot assign product '1' to more than 2 warehouses"));
    }

    @Test
    @Transactional
    void testStoreWarehouseConstraintExceeded() {
        createFulfilment(1, 1, "MWH.001");
        createFulfilment(2, 1, "MWH.012");
        createFulfilment(3, 1, "MWH.023");

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                usecase.assignFulfilment(4, 1, "MWH.024")
        );

        assertTrue(ex.getMessage().contains("Cannot assign more than 3 warehouses for store"));
    }

    @Test
    @Transactional
    void testWarehouseProductConstraintExceeded() {
        createFulfilment(1, 1, "MWH.001");
        createFulfilment(2, 2, "MWH.001");
        createFulfilment(3, 3, "MWH.001");
        createFulfilment(4, 4, "MWH.001");
        createFulfilment(5, 5, "MWH.001");

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                usecase.assignFulfilment(6, 6, "MWH.001")
        );

        assertTrue(ex.getMessage().contains("Warehouse 'MWH.001' cannot store more than 5 different products"));
    }

    @Test
    @Transactional
    void testMultipleSuccessfulAssignments() {
        usecase.assignFulfilment(1, 1, "MWH.001");
        usecase.assignFulfilment(1, 1, "MWH.012");
        usecase.assignFulfilment(2, 1, "MWH.001");

        assertEquals(2, repository.countByProductAndStore(1, 1));
        assertEquals(1, repository.countByProductAndStore(2, 1));
        assertEquals(2, repository.countDistinctWarehousesByStore(1));
    }

    @Test
    @Transactional
    void testInvalidWarehouse() {

        UnknownWarehouseException ex = assertThrows(
                UnknownWarehouseException.class,
                () -> usecase.assignFulfilment(1, 1, "INVALID")
        );

        assertNotNull(ex);
    }

    @Test
    @Transactional
    void testStoreNotFound() {

        BusinessLogicException ex = assertThrows(
                BusinessLogicException.class,
                () -> usecase.assignFulfilment(1, 999, "MWH.001")
        );

        assertEquals("Store not found", ex.getMessage());
    }

    @Test
    @Transactional
    void testProductNotFound() {

        BusinessLogicException ex = assertThrows(
                BusinessLogicException.class,
                () -> usecase.assignFulfilment(999, 1, "MWH.001")
        );

        assertEquals("Product not found", ex.getMessage());
    }
}