package com.fulfilment.application.monolith.warehouses.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;

public record WarehouseRecord(String id, String businessUnitCode, String location, Integer capacity, Integer stock,
                              LocalDateTime createdAt, LocalDateTime archivedAt) {

    // unique identifier
    // TODO: none in the provided code  supports the idea that this is a unique identifier
    // Examples in provided API suggest  taht id is numeric value different from businessUnitCode which makes more sense to me
    // That also means that his bean is partially implemented and requires additioanl id field

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WarehouseRecord that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(businessUnitCode, that.businessUnitCode) && Objects.equals(location, that.location) && Objects.equals(capacity, that.capacity) && Objects.equals(stock, that.stock) && Objects.equals(createdAt, that.createdAt) && Objects.equals(archivedAt, that.archivedAt);
    }

}
