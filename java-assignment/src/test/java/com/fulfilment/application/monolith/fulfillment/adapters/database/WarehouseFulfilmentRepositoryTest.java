package com.fulfilment.application.monolith.fulfillment.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseFulfilmentRepositoryTest {

    @Inject
    WarehouseFulfilmentRepository repository;

    @BeforeEach
    @Transactional
    void setup() {
        // Clear table before each test
        repository.deleteAll();
    }

    private WarehouseFulfilment createFulfilment(String product, String store, String warehouse) {
        WarehouseFulfilment wf = new WarehouseFulfilment();
        wf.product = product;
        wf.store = store;
        wf.warehouse = warehouse;
        wf.createdAt = LocalDateTime.now();
        wf.updatedAt = LocalDateTime.now();
        repository.save(wf);
        return wf;
    }

    @Test
    @Transactional
    void testSaveAndExists() {
        createFulfilment("ProductA", "Store1", "Warehouse1");

        assertTrue(repository.existsByProductStoreWarehouse("ProductA", "Store1", "Warehouse1"));
        assertFalse(repository.existsByProductStoreWarehouse("ProductA", "Store1", "Warehouse2"));
    }

    @Test
    @Transactional
    void testCountByProductAndStore() {
        createFulfilment("ProductA", "Store1", "Warehouse1");
        createFulfilment("ProductA", "Store1", "Warehouse2");

        long count = repository.countByProductAndStore("ProductA", "Store1");
        assertEquals(2, count);
    }

    @Test
    @Transactional
    void testCountDistinctWarehousesByStore() {
        createFulfilment("ProductA", "Store1", "Warehouse1");
        createFulfilment("ProductB", "Store1", "Warehouse2");
        createFulfilment("ProductC", "Store1", "Warehouse1"); // same warehouse

        long distinctCount = repository.countDistinctWarehousesByStore("Store1");
        assertEquals(2, distinctCount); // Warehouse1 & Warehouse2
    }

    @Test
    @Transactional
    void testCountByWarehouse() {
        createFulfilment("ProductA", "Store1", "Warehouse1");
        createFulfilment("ProductB", "Store2", "Warehouse1");
        createFulfilment("ProductC", "Store1", "Warehouse2");

        long warehouse1Count = repository.countByWarehouse("Warehouse1");
        long warehouse2Count = repository.countByWarehouse("Warehouse2");

        assertEquals(2, warehouse1Count);
        assertEquals(1, warehouse2Count);
    }
}