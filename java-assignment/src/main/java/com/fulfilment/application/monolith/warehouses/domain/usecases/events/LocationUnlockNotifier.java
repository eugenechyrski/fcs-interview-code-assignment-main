package com.fulfilment.application.monolith.warehouses.domain.usecases.events;

import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocationUnlockNotifier {

    @Inject
    LocationResolver locationResolver;

    public void onWarehouseCreationCommit(
            @Observes(during = TransactionPhase.AFTER_SUCCESS)
            WarehouseCreationEvent event) {
        locationResolver.unlock(event.location());
    }
}
