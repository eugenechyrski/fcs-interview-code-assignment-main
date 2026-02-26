package com.fulfilment.application.monolith.warehouses.domain.usecases.events;

import java.util.Objects;

public record WarehouseCreationEvent(String location) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WarehouseCreationEvent that)) return false;
        return Objects.equals(location, that.location);
    }

}
