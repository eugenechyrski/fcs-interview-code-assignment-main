package com.fulfilment.application.monolith.warehouses.adapters.database;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse")
@Cacheable
class DbWarehouse {

    @Id
    @GeneratedValue
    public Long id;

    public String businessUnitCode;

    public String location;

    public Integer capacity;

    public Integer stock;

    public LocalDateTime createdAt;

    public LocalDateTime archivedAt;

}
