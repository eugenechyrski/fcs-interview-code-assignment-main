package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

@ApplicationScoped
public class WarehouseMapper {
    public DbWarehouse toDbWarehouse(WarehouseRecord warehouse) {
        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.businessUnitCode = warehouse.businessUnitCode();
        dbWarehouse.location = warehouse.location();
        dbWarehouse.createdAt = warehouse.createdAt();
        dbWarehouse.archivedAt = warehouse.archivedAt();
        dbWarehouse.stock = warehouse.stock();
        dbWarehouse.capacity = warehouse.capacity();
        if (warehouse.id() != null) {
            throw new RuntimeException("Warehouse id propagated up to conversion logic");
        }
        dbWarehouse.createdAt = LocalDateTime.now();
        return dbWarehouse;
    }

    public WarehouseRecord toWarehouse(DbWarehouse dbWarehouse) {
        return new WarehouseRecord(
                String.valueOf(dbWarehouse.id),
                dbWarehouse.businessUnitCode,
                dbWarehouse.location,
                dbWarehouse.capacity,
                dbWarehouse.stock,
                dbWarehouse.createdAt,
                dbWarehouse.archivedAt
        );
    }
}
