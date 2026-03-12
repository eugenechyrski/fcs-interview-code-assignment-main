package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ListWarehousesUseCaseTest {

    @Inject
    ListWarehousesUseCase listWarehousesUseCase;

    @InjectMock
    WarehouseStore warehouseStore;

    @Test
    void shouldReturnOnlyActiveWarehousesWhenArchivedNotIncluded() {
        // given
        WarehouseRecord activeWarehouse = new WarehouseRecord(
                "1", "BU-1", "Berlin", 1000, 200,
                LocalDateTime.now().minusDays(5),
                null
        );

        WarehouseRecord archivedWarehouse = new WarehouseRecord(
                "2", "BU-2", "Hamburg", 1500, 300,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1)
        );

        when(warehouseStore.getAll())
                .thenReturn(List.of(activeWarehouse, archivedWarehouse));

        // when
        Collection<WarehouseRecord> result = listWarehousesUseCase.list(false);

        // then
        verify(warehouseStore).getAll();
        assertEquals(1, result.size());
        assertTrue(result.contains(activeWarehouse));
        assertFalse(result.contains(archivedWarehouse));
    }

    @Test
    void shouldReturnAllWarehousesWhenArchivedIncluded() {
        // given
        WarehouseRecord activeWarehouse = new WarehouseRecord(
                "1", "BU-1", "Berlin", 1000, 200,
                LocalDateTime.now().minusDays(5),
                null
        );

        WarehouseRecord archivedWarehouse = new WarehouseRecord(
                "2", "BU-2", "Hamburg", 1500, 300,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(1)
        );

        when(warehouseStore.getAll())
                .thenReturn(List.of(activeWarehouse, archivedWarehouse));

        // when
        Collection<WarehouseRecord> result = listWarehousesUseCase.list(true);

        // then
        verify(warehouseStore).getAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(activeWarehouse));
        assertTrue(result.contains(archivedWarehouse));
    }

    @Test
    void shouldReturnEmptyListWhenNoWarehousesExist() {
        // given
        when(warehouseStore.getAll()).thenReturn(List.of());

        // when
        Collection<WarehouseRecord> result = listWarehousesUseCase.list(false);

        // then
        verify(warehouseStore).getAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}