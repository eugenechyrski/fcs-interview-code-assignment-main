package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;

public record StoreChangedEvent(Store store, StoreOperation storeOperation) {

}
