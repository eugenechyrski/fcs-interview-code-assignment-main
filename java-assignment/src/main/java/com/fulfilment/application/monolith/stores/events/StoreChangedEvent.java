package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;

import java.util.Objects;

public record StoreChangedEvent(Store store, StoreOperation storeOperation) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StoreChangedEvent that)) return false;
        return Objects.equals(store, that.store) && storeOperation == that.storeOperation;
    }

}
