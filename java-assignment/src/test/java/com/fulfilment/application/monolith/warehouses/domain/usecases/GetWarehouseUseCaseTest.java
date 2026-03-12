package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class GetWarehouseUseCaseTest {

    @Inject
    GetWarehouseUseCase getWarehouseUseCase;

    @InjectMock
    WarehouseStore warehouseStore;

    @Test
    void shouldReturnWarehouseWhenGetActiveById() {
        // given
        WarehouseRecord warehouse = new WarehouseRecord(
                "1",
                "BU-1",
                "Berlin",
                1000,
                200,
                LocalDateTime.now().minusDays(3),
                null
        );

        when(warehouseStore.findById(1L)).thenReturn(warehouse);

        // when
        WarehouseRecord result = getWarehouseUseCase.getActiveById("1");

        // then
        verify(warehouseStore).findById(1L);
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("BU-1", result.businessUnitCode());
        assertEquals("Berlin", result.location());
    }

    @Test
    void shouldReturnWarehouseWhenGetActiveByUnitId() {
        // given
        WarehouseRecord warehouse = new WarehouseRecord(
                "2",
                "BU-99",
                "Hamburg",
                1500,
                300,
                LocalDateTime.now().minusDays(2),
                null
        );

        when(warehouseStore.findByBusinessUnitCode("BU-99")).thenReturn(warehouse);

        // when
        WarehouseRecord result = getWarehouseUseCase.getActiveByUnitId("BU-99");

        // then
        verify(warehouseStore).findByBusinessUnitCode("BU-99");
        assertNotNull(result);
        assertEquals("2", result.id());
        assertEquals("BU-99", result.businessUnitCode());
        assertEquals("Hamburg", result.location());
    }

    @Test
    void shouldReturnNullWhenWarehouseNotFoundById() {
        // given
        when(warehouseStore.findById(1L)).thenReturn(null);

        // when
        WarehouseRecord result = getWarehouseUseCase.getActiveById("1");

        // then
        verify(warehouseStore).findById(1L);
        assertNull(result);
    }

    @Test
    void shouldReturnNullWhenWarehouseNotFoundByUnitId() {
        // given
        when(warehouseStore.findByBusinessUnitCode("BU-X")).thenReturn(null);

        // when
        WarehouseRecord result = getWarehouseUseCase.getActiveByUnitId("BU-X");

        // then
        verify(warehouseStore).findByBusinessUnitCode("BU-X");
        assertNull(result);
    }
}