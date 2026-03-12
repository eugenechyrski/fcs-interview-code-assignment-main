package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.*;
import com.warehouse.api.beans.Warehouse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class WarehouseResourceImplTest {

    @Inject
    WarehouseResourceImpl resource;

    @InjectMock
    CreateWarehouseOperation createWarehouseOperation;

    @InjectMock
    ListWarehousesOperation listWarehouseOperation;

    @InjectMock
    ArchiveWarehouseOperation archiveWarehouseOperation;

    @InjectMock
    ReplaceWarehouseOperation replaceWarehouseOperation;

    @InjectMock
    GetWarehouseOperation getWarehouseOperation;

    @Test
    void shouldListAllWarehouses() {
        WarehouseRecord record = new WarehouseRecord(
                "1", "BU-1", "Amsterdam", 1000, 200,
                LocalDateTime.now(), null
        );

        when(listWarehouseOperation.list(false)).thenReturn(List.of(record));

        List<Warehouse> result = resource.listAllWarehousesUnits();

        assertEquals(1, result.size());
        assertEquals("BU-1", result.get(0).getBusinessUnitCode());
        verify(listWarehouseOperation).list(false);
    }

    @Test
    void shouldCreateWarehouse() {
        Warehouse request = new Warehouse();
        request.setBusinessUnitCode("BU-2");
        request.setLocation("Zwolle");
        request.setCapacity(1000);
        request.setStock(100);

        WarehouseRecord stored = new WarehouseRecord(
                "5", "BU-2", "Zwolle", 1000, 100,
                LocalDateTime.now(), null
        );

        when(getWarehouseOperation.getActiveByUnitId("BU-2")).thenReturn(stored);

        Warehouse response = resource.createANewWarehouseUnit(request);

        verify(createWarehouseOperation).create(any());
        verify(getWarehouseOperation).getActiveByUnitId("BU-2");

        assertEquals("5", response.getId());
        assertEquals("BU-2", response.getBusinessUnitCode());
    }

    @Test
    void shouldThrow422WhenCreatingWarehouseWithId() {
        Warehouse request = new Warehouse();
        request.setId("10");

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> resource.createANewWarehouseUnit(request)
        );

        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    void shouldGetWarehouseById() {
        WarehouseRecord record = new WarehouseRecord(
                "7", "BU-7", "Tilburg", 900, 150,
                LocalDateTime.now(), null
        );

        when(getWarehouseOperation.getActiveById("7")).thenReturn(record);

        Warehouse response = resource.getAWarehouseUnitByID("7");

        verify(getWarehouseOperation).getActiveById("7");
        assertEquals("7", response.getId());
        assertEquals("BU-7", response.getBusinessUnitCode());
    }

    @Test
    void shouldArchiveWarehouse() {
        resource.archiveAWarehouseUnitByID("3");

        verify(archiveWarehouseOperation)
                .archive(argThat(w -> "3".equals(w.id())));
    }

    @Test
    void shouldReplaceWarehouse() {
        Warehouse request = new Warehouse();
        request.setBusinessUnitCode("BU-9");
        request.setLocation("Eindhoven");
        request.setCapacity(500);
        request.setStock(50);

        WarehouseRecord updated = new WarehouseRecord(
                "9", "BU-9", "Eindhoven", 500, 50,
                LocalDateTime.now(), null
        );

        when(getWarehouseOperation.getActiveByUnitId("BU-9")).thenReturn(updated);

        Warehouse response = resource.replaceTheCurrentActiveWarehouse("BU-9", request);

        verify(replaceWarehouseOperation).replace(any());
        verify(getWarehouseOperation).getActiveByUnitId("BU-9");

        assertEquals("9", response.getId());
    }

    @Test
    void shouldThrow422WhenBusinessUnitCodeMismatch() {
        Warehouse request = new Warehouse();
        request.setBusinessUnitCode("BU-10");

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> resource.replaceTheCurrentActiveWarehouse("OTHER", request)
        );

        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    void shouldThrow422WhenReplaceContainsId() {
        Warehouse request = new Warehouse();
        request.setBusinessUnitCode("BU-10");
        request.setId("5");

        WebApplicationException ex = assertThrows(
                WebApplicationException.class,
                () -> resource.replaceTheCurrentActiveWarehouse("BU-10", request)
        );

        assertEquals(422, ex.getResponse().getStatus());
    }
}