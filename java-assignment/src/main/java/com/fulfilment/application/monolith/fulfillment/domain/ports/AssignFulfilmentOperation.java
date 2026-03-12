package com.fulfilment.application.monolith.fulfillment.domain.ports;

public interface AssignFulfilmentOperation {
    void assignFulfilment(long productId, long storeId, String warehouse);
}
