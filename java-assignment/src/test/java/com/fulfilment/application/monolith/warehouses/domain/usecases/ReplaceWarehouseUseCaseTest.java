package com.fulfilment.application.monolith.warehouses.domain.usecases;
import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.GetWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ReplaceWarehouseUseCaseTest {

    @Inject
    ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @InjectMock
    ArchiveWarehouseOperation archiveWarehouseOperation;

    @InjectMock
    CreateWarehouseOperation createWarehouseOperation;

    @InjectMock
    GetWarehouseOperation getWarehouseOperation;

    @Test
    void shouldReplaceWarehouseSuccessfully() throws Exception {
        // given
        WarehouseRecord existing = new WarehouseRecord(
                "1",
                "BU-1",
                "Berlin",
                1000,
                300,
                LocalDateTime.now().minusDays(10),
                null
        );

        WarehouseRecord newWarehouse = new WarehouseRecord(
                null,
                "BU-1",
                "Munich",
                1500,
                300,
                null,
                null
        );

        when(getWarehouseOperation.getActiveByUnitId("BU-1"))
                .thenReturn(existing);

        // when
        replaceWarehouseUseCase.replace(newWarehouse);

        // then
        verify(getWarehouseOperation).getActiveByUnitId("BU-1");
        verify(archiveWarehouseOperation).archive(existing);
        verify(createWarehouseOperation).create(newWarehouse);
    }

    @Test
    void shouldThrowWhenWarehouseIsNull() {
        BusinessLogicException exception = assertThrows(
                BusinessLogicException.class,
                () -> replaceWarehouseUseCase.replace(null)
        );

        assertEquals("Warehouse data should be provided", exception.getMessage());

        verifyNoInteractions(getWarehouseOperation);
        verifyNoInteractions(archiveWarehouseOperation);
        verifyNoInteractions(createWarehouseOperation);
    }

    @Test
    void shouldThrowWhenWarehouseContainsId() {
        WarehouseRecord invalid = new WarehouseRecord(
                "99",
                "BU-1",
                "Berlin",
                1000,
                200,
                null,
                null
        );

        BusinessLogicException exception = assertThrows(
                BusinessLogicException.class,
                () -> replaceWarehouseUseCase.replace(invalid)
        );

        assertEquals("Warehouse data should not cointain identifier", exception.getMessage());

        verifyNoInteractions(getWarehouseOperation);
        verifyNoInteractions(archiveWarehouseOperation);
        verifyNoInteractions(createWarehouseOperation);
    }

    @Test
    void shouldPropagateUnknownWarehouseException() throws Exception {
        WarehouseRecord newWarehouse = new WarehouseRecord(
                null,
                "BU-404",
                "Hamburg",
                500,
                50,
                null,
                null
        );

        when(getWarehouseOperation.getActiveByUnitId("BU-404"))
                .thenThrow(new UnknownWarehouseException());

        assertThrows(
                UnknownWarehouseException.class,
                () -> replaceWarehouseUseCase.replace(newWarehouse)
        );

        verify(createWarehouseOperation, never()).create(any());
        verify(archiveWarehouseOperation, never()).archive(any());
    }
}