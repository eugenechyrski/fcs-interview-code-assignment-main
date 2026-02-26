package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

@ApplicationScoped
public class LegacyStoreNotifier {
    @Inject
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    public void onStoreChanged(
            @Observes(during = TransactionPhase.AFTER_SUCCESS)
            StoreChangedEvent event) {
        try {
            switch (event.storeOperation()) {
                case CREATE -> legacyStoreManagerGateway.createStoreOnLegacySystem(event.store());
                case UPDATE -> legacyStoreManagerGateway.updateStoreOnLegacySystem(event.store());
            }
        } catch (Exception e) {
            // At this point, the database transaction has already been committed,
            // but the legacy system update failed.
            // These records should be flagged for retry, and a separate service
            // should handle re-sending them to ensure eventual consistency.
            // I consider  this implementayion out of scope for this assignment
        }
    }
}
