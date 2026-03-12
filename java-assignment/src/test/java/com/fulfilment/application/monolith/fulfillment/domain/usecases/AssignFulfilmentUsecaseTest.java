package com.fulfilment.application.monolith.fulfillment.domain.usecases;

import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilment;
import com.fulfilment.application.monolith.fulfillment.adapters.database.WarehouseFulfilmentRepository;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
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

    private WarehouseFulfilment createFulfilment(String product, String store, String warehouse) {
        WarehouseFulfilment wf = new WarehouseFulfilment();
        wf.product = product;
        wf.store = store;
        wf.warehouse = warehouse;
        wf.createdAt = LocalDateTime.now();
        wf.updatedAt = LocalDateTime.now();
        repository.persist(wf);
        return wf;
    }

    @Test
    @Transactional
    void testAssignFulfilmentSuccess() {
        usecase.assignFulfilment("ProductA", "Store1", "Warehouse1");

        boolean exists = repository.existsByProductStoreWarehouse("ProductA", "Store1", "Warehouse1");
        assertTrue(exists);
    }

    @Test
    @Transactional
    void testAssignDuplicateFulfilment() {
        createFulfilment("ProductA", "Store1", "Warehouse1");

        // Should not throw, just log and skip
        usecase.assignFulfilment("ProductA", "Store1", "Warehouse1");

        long count = repository.countByProductAndStore("ProductA", "Store1");
        assertEquals(1, count);
    }

    @Test
    @Transactional
    void testProductWarehouseConstraintExceeded() {
        createFulfilment("ProductA", "Store1", "Warehouse1");
        createFulfilment("ProductA", "Store1", "Warehouse2");

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                usecase.assignFulfilment("ProductA", "Store1", "Warehouse3")
        );

        assertTrue(ex.getMessage().contains("Cannot assign product 'ProductA' to more than 2 warehouses"));
    }

    @Test
    @Transactional
    void testStoreWarehouseConstraintExceeded() {
        createFulfilment("ProductA", "Store1", "Warehouse1");
        createFulfilment("ProductB", "Store1", "Warehouse2");
        createFulfilment("ProductC", "Store1", "Warehouse3");

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                usecase.assignFulfilment("ProductD", "Store1", "Warehouse4")
        );

        assertTrue(ex.getMessage().contains("Cannot assign more than 3 warehouses for store"));
    }

    @Test
    @Transactional
    void testWarehouseProductConstraintExceeded() {
        createFulfilment("ProductA", "Store1", "Warehouse1");
        createFulfilment("ProductB", "Store2", "Warehouse1");
        createFulfilment("ProductC", "Store3", "Warehouse1");
        createFulfilment("ProductD", "Store4", "Warehouse1");
        createFulfilment("ProductE", "Store5", "Warehouse1");

        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                usecase.assignFulfilment("ProductF", "Store6", "Warehouse1")
        );

        assertTrue(ex.getMessage().contains("Warehouse 'Warehouse1' cannot store more than 5 different products"));
    }

    @Test
    @Transactional
    void testMultipleSuccessfulAssignments() {
        usecase.assignFulfilment("ProductA", "Store1", "Warehouse1");
        usecase.assignFulfilment("ProductA", "Store1", "Warehouse2");
        usecase.assignFulfilment("ProductB", "Store1", "Warehouse1");

        assertEquals(2, repository.countByProductAndStore("ProductA", "Store1"));
        assertEquals(1, repository.countByProductAndStore("ProductB", "Store1"));
        assertEquals(2, repository.countDistinctWarehousesByStore("Store1"));
    }
}