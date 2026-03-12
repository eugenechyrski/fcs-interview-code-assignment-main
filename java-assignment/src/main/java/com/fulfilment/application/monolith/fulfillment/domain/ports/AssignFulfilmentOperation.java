package com.fulfilment.application.monolith.fulfillment.domain.ports;

public interface AssignFulfilmentOperation {
    void assignFulfilment(String product, String store, String warehouse);
}
