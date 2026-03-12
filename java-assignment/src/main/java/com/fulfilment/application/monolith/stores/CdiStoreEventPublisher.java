package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.stores.events.StoreChangedEvent;
import com.fulfilment.application.monolith.stores.events.StoreOperation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class CdiStoreEventPublisher {
    @Inject
    Event<StoreChangedEvent> event;

    public void storeChanged(Store store, StoreOperation storeOperation) {
        event.fire(new StoreChangedEvent(store, storeOperation));
    }
}

