package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class WarehouseMapperTest {

    @Inject
    WarehouseMapper mapper;

    @Test
    void shouldMapWarehouseRecordToDbWarehouse() {
        // given
        WarehouseRecord record = new WarehouseRecord(
                null,
                "BU-1",
                "Amsterdam",
                1000,
                200,
                LocalDateTime.now().minusDays(1),
                null
        );

        // when
        DbWarehouse dbWarehouse = mapper.toDbWarehouse(record);

        // then
        assertEquals("BU-1", dbWarehouse.businessUnitCode);
        assertEquals("Amsterdam", dbWarehouse.location);
        assertEquals(200, dbWarehouse.stock);
        assertEquals(1000, dbWarehouse.capacity);
        assertNotNull(dbWarehouse.createdAt);
    }

    @Test
    void shouldThrowExceptionWhenIdIsPresentDuringMappingToDb() {
        // given
        WarehouseRecord record = new WarehouseRecord(
                "1",
                "BU-1",
                "Amsterdam",
                1000,
                200,
                LocalDateTime.now(),
                null
        );

        // when / then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> mapper.toDbWarehouse(record)
        );

        assertTrue(exception.getMessage().contains("Warehouse id propagated"));
    }

    @Test
    void shouldMapDbWarehouseToWarehouseRecord() {
        // given
        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.id = 5L;
        dbWarehouse.businessUnitCode = "BU-5";
        dbWarehouse.location = "Zwolle";
        dbWarehouse.capacity = 1200;
        dbWarehouse.stock = 300;
        dbWarehouse.createdAt = LocalDateTime.now().minusDays(2);
        dbWarehouse.archivedAt = null;

        // when
        WarehouseRecord record = mapper.toWarehouse(dbWarehouse);

        // then
        assertEquals("5", record.id());
        assertEquals("BU-5", record.businessUnitCode());
        assertEquals("Zwolle", record.location());
        assertEquals(1200, record.capacity());
        assertEquals(300, record.stock());
        assertEquals(dbWarehouse.createdAt, record.createdAt());
        assertNull(record.archivedAt());
    }
}