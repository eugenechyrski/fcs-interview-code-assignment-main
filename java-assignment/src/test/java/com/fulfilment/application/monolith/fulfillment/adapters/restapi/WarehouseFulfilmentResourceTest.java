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
        request.setProduct(1);
        request.setStore(1);
        request.setWarehouse("MWH.001");

        // When
        resource.assignFulfilment(request);

        // Then
        verify(assignFulfilmentOperation, times(1))
                .assignFulfilment(1, 1, "MWH.001");
    }
}