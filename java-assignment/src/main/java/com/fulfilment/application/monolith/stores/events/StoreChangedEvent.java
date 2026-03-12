package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;

import java.util.Objects;

public record StoreChangedEvent(Store store, StoreOperation storeOperation) {

}
