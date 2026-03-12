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

}

