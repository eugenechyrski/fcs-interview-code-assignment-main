package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ArchiveWarehouseUseCaseTest {

    @Inject
    ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @InjectMock
    GetWarehouseOperation getWarehouseOperation;

    @InjectMock
    WarehouseStore warehouseStore;

    @Test
    void shouldArchiveWarehouseSuccessfully() throws UnknownWarehouseException {
        // given
        WarehouseRecord activeWarehouse = new WarehouseRecord(
                "1",
                "BU-1",
                "Berlin",
                1000,
                200,
                LocalDateTime.now().minusDays(5),
                null
        );

        when(getWarehouseOperation.getActiveById("1"))
                .thenReturn(activeWarehouse);

        // when
        archiveWarehouseUseCase.archive(activeWarehouse);

        // then
        verify(getWarehouseOperation).getActiveById("1");

        ArgumentCaptor<WarehouseRecord> captor =
                ArgumentCaptor.forClass(WarehouseRecord.class);

        verify(warehouseStore).update(captor.capture());

        WarehouseRecord archived = captor.getValue();

        assertNotNull(archived.archivedAt());
        assertEquals(activeWarehouse.id(), archived.id());
        assertEquals(activeWarehouse.createdAt(), archived.createdAt());
    }

    @Test
    void shouldThrowExceptionWhenWarehouseNotFound() throws UnknownWarehouseException {
        WarehouseRecord request = new WarehouseRecord(
                "1", "BU-1", "Berlin", 1000, 200,
                LocalDateTime.now(), null
        );

        when(getWarehouseOperation.getActiveById("1"))
                .thenThrow(new UnknownWarehouseException());

        assertThrows(
                UnknownWarehouseException.class,
                () -> archiveWarehouseUseCase.archive(request)
        );

        verify(warehouseStore, never()).update(any());
    }
}