package com.fulfilment.application.monolith.fulfillment.adapters.database;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "warehouse_fulfilment")
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product", "store", "warehouse"}
        )
)
@Cacheable
public class WarehouseFulfilment {

    @Id
    @GeneratedValue
    public Long id;
    public String product;
    public String store;
    public String warehouse;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WarehouseFulfilment that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(product, that.product) && Objects.equals(store, that.store) && Objects.equals(warehouse, that.warehouse) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, store, warehouse, createdAt, updatedAt);
    }
}

