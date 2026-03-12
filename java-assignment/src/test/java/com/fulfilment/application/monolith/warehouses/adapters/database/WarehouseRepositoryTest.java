package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.exception.DuplicateWarehouseException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class WarehouseRepositoryTest {

    @Inject
    WarehouseRepository repository;

    @Test
    @Transactional
    void shouldCreateWarehouse() {
        WarehouseRecord warehouse = new WarehouseRecord(
                null,
                "BU-TEST-1",
                "Amsterdam",
                1000,
                200,
                LocalDateTime.now(),
                null
        );

        repository.create(warehouse);

        WarehouseRecord result = repository.findByBusinessUnitCode("BU-TEST-1");

        assertNotNull(result);
        assertEquals("BU-TEST-1", result.businessUnitCode());
        assertEquals("Amsterdam", result.location());
    }

    @Test
    @Transactional
    void shouldThrowDuplicateWarehouseExceptionWhenCreatingDuplicate() {
        WarehouseRecord warehouse = new WarehouseRecord(
                null,
                "BU-DUP",
                "Zwolle",
                1000,
                200,
                LocalDateTime.now(),
                null
        );

        repository.create(warehouse);

        assertThrows(
                DuplicateWarehouseException.class,
                () -> repository.create(warehouse)
        );
    }

    @Test
    @Transactional
    void shouldFindWarehouseById() {
        WarehouseRecord warehouse = new WarehouseRecord(
                null,
                "BU-ID",
                "Tilburg",
                800,
                100,
                LocalDateTime.now(),
                null
        );

        repository.create(warehouse);

        WarehouseRecord stored = repository.findByBusinessUnitCode("BU-ID");
        WarehouseRecord result = repository.findById(Long.parseLong(stored.id()));

        assertEquals("BU-ID", result.businessUnitCode());
    }

    @Test
    @Transactional
    void shouldFindWarehousesByLocation() {
        repository.create(new WarehouseRecord(null,"BU-L1","Eindhoven",500,50,LocalDateTime.now(),null));
        repository.create(new WarehouseRecord(null,"BU-L2","Eindhoven",600,60,LocalDateTime.now(),null));

        List<WarehouseRecord> result = repository.findByLocation("Eindhoven");

        assertEquals(2, result.size());
    }


    @Test
    @Transactional
    void shouldRemoveWarehouse() {
        repository.create(new WarehouseRecord(null,"BU-DEL","Paris",500,50,LocalDateTime.now(),null));

        WarehouseRecord stored = repository.findByBusinessUnitCode("BU-DEL");

        repository.remove(stored);

        assertThrows(
                UnknownWarehouseException.class,
                () -> repository.findByBusinessUnitCode("BU-DEL")
        );
    }

    @Test
    @Transactional
    void shouldReturnAllWarehouses() {
        repository.create(new WarehouseRecord(null,"BU-A1","Rome",400,30,LocalDateTime.now(),null));
        repository.create(new WarehouseRecord(null,"BU-A2","Rome",600,40,LocalDateTime.now(),null));

        List<WarehouseRecord> result = repository.getAll();

        assertTrue(result.size() >= 2);
    }
}