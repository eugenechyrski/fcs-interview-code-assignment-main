package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;

public interface LocationResolver {
    Location resolveByIdentifier(String identifier);

    /**
     * For single jvm outside k8s  should be fine with
     *
     * @param identifier
     * @return
     */
    Location resolveByIdentifierAndLock(String identifier);

    void unlock(String identifier);

}
