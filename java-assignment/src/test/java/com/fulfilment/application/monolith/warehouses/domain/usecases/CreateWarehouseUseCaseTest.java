package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseEventPublisher;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class CreateWarehouseUseCaseTest {

    @Inject
    CreateWarehouseUseCase useCase;

    @InjectMock
    LocationResolver locationResolver;

    @InjectMock
    WarehouseStore warehouseStore;

    @InjectMock
    WarehouseEventPublisher eventPublisher;


    @Test
    void shouldCreateSuccessfully() {
        when(locationResolver.resolveByIdentifierAndLock("LOC-1"))
                .thenReturn(new Location("LOC-1", 10_000, 200));

        when(warehouseStore.findByLocation("LOC-1"))
                .thenReturn(List.of());

        useCase.create(new WarehouseRecord(
                null,
                "BU-1",
                "LOC-1",
                200,
                200,
                LocalDateTime.now(),
                null
        ));

        verify(warehouseStore).create(any());
        verify(eventPublisher).warehouseCreated("LOC-1");
    }

    @Test
    void shouldFailWhenMaxWarehousesReached() {
        when(locationResolver.resolveByIdentifierAndLock("LOC-1"))
                .thenReturn(new Location("LOC-1", 10_000, 1));

        when(warehouseStore.findByLocation("LOC-1"))
                .thenReturn(List.of(
                        new WarehouseRecord("1", "BU-0", "LOC-1", 500, 100, LocalDateTime.now(), null)
                ));

        assertThrows(
                BusinessLogicException.class,
                () -> useCase.create(new WarehouseRecord(
                        null,
                        "BU-1",
                        "LOC-1",
                        1000,
                        200,
                        LocalDateTime.now(),
                        null
                ))
        );

        verify(warehouseStore, never()).create(any());
        verify(eventPublisher).warehouseCreated("LOC-1");
    }

    @Test
    void shouldFailWhenCapacityExceedsLocationLimit() {
        Location location = new Location("LOC-1", 500, 5);

        when(locationResolver.resolveByIdentifierAndLock("LOC-1"))
                .thenReturn(location);

        when(warehouseStore.findByLocation("LOC-1"))
                .thenReturn(List.of());

        WarehouseRecord warehouse = new WarehouseRecord(
                null, "BU-1", "LOC-1",
                1000, 200,
                LocalDateTime.now(), null
        );

        assertThrows(
                BusinessLogicException.class,
                () -> useCase.create(warehouse)
        );

        verify(warehouseStore, never()).create(any());
    }

    @Test
    void shouldFailWhenStockExceedsCapacity() {
        when(locationResolver.resolveByIdentifierAndLock("LOC-1"))
                .thenReturn(new Location("LOC-1", 10_000, 2));

        when(warehouseStore.findByLocation("LOC-1"))
                .thenReturn(List.of());

        WarehouseRecord warehouse = new WarehouseRecord(
                null, "BU-1", "LOC-1",
                1000, 1500,
                LocalDateTime.now(), null
        );

        assertThrows(
                BusinessLogicException.class,
                () -> useCase.create(warehouse)
        );
    }

    @Test
    void shouldFailWhenWarehouseIsNull() {
        assertThrows(
                BusinessLogicException.class,
                () -> useCase.create(null)
        );

        verifyNoInteractions(locationResolver);
        verifyNoInteractions(warehouseStore);
    }
}