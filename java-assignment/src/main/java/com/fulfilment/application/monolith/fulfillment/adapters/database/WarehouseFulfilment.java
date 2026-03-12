package com.fulfilment.application.monolith.fulfillment.adapters.database;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "warehouse_fulfilment")
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"productId", "storeId", "warehouse"}
        )
)
@Cacheable
public class WarehouseFulfilment {

    @Id
    @GeneratedValue
    public Long id;
    public Long productId;
    public Long storeId;
    public String warehouse;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

}

