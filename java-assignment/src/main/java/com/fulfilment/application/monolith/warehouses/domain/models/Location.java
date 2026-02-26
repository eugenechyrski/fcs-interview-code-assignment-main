package com.fulfilment.application.monolith.warehouses.domain.models;

import java.util.Objects;

/**
 * @param maxNumberOfWarehouses maximum number of warehouses that can be created in this location
 * @param maxCapacity           maximum capacity of the location summing all the warehouse capacities
 */
public record Location(String identification, int maxNumberOfWarehouses, int maxCapacity) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Location location)) return false;
        return maxNumberOfWarehouses == location.maxNumberOfWarehouses && maxCapacity == location.maxCapacity && Objects.equals(identification, location.identification);
    }

}
