package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fulfilment.application.monolith.fulfillment.domain.ports.AssignFulfilmentOperation;
import com.warehouse.api.beans.WarehouseFulfilmentRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
@QuarkusTest
class WarehouseFulfilmentResourceTest {

    private AssignFulfilmentOperation assignFulfilmentOperation;
    private WarehouseFulfilmentResource resource;

    @BeforeEach
    void setup() {
        assignFulfilmentOperation = mock(AssignFulfilmentOperation.class);
        resource = new WarehouseFulfilmentResource();
        resource.assignFulfilmentOperation = assignFulfilmentOperation; // inject mock
    }

    @Test
    void testAssignFulfilment_callsAssignFulfilmentOperation() {
        // Given
        WarehouseFulfilmentRequest request = new WarehouseFulfilmentRequest();
        request.setProduct("ProductA");
        request.setStore("Store1");
        request.setWarehouse("Warehouse1");

        // When
        resource.assignFulfilment(request);

        // Then
        verify(assignFulfilmentOperation, times(1))
                .assignFulfilment("ProductA", "Store1", "Warehouse1");
    }
}