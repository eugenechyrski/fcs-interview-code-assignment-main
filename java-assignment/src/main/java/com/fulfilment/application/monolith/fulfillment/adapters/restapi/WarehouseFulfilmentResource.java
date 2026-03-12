package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.fulfilment.application.monolith.fulfillment.domain.ports.AssignFulfilmentOperation;
import com.warehouse.api.FulfilmentResource;
import com.warehouse.api.beans.WarehouseFulfilmentRequest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;


@RequestScoped
public class WarehouseFulfilmentResource implements FulfilmentResource {
    @Inject
    AssignFulfilmentOperation assignFulfilmentOperation;


    @Override
    public void assignFulfilment(WarehouseFulfilmentRequest request) {
        assignFulfilmentOperation.assignFulfilment(
                request.getProduct(),
                request.getStore(),
                request.getWarehouse()
        );
    }
}


