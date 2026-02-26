package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.exception.DuplicateWarehouseException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {
    private static final String UPDATE_SQL = """
                    location = ?1, 
                    capacity = ?2, 
                    stock = ?3, 
                    archivedAt = ?4 
                    WHERE 
                    businessUnitCode = ?5 AND
                    archivedAt is null
            """;

    @Inject
    WarehouseMapper mapper;

    @Override
    public List<WarehouseRecord> getAll() {
        return this.listAll().stream().map(mapper::toWarehouse).toList();
    }

    @Override
    public void create(WarehouseRecord warehouse) {
        DbWarehouse dbWarehouse = findDbWarehouseByBusinessUnitCode(warehouse.businessUnitCode());
        checkDbWarehouseAbsent(dbWarehouse);
        persist(mapper.toDbWarehouse(warehouse));
    }

    @Override
    public void update(WarehouseRecord warehouse) throws UnknownWarehouseException {
        findDbWarehouseByBusinessUnitCode(warehouse.businessUnitCode());
        update(UPDATE_SQL,
                warehouse.location(),
                warehouse.capacity(),
                warehouse.stock(),
                warehouse.archivedAt(),
                warehouse.businessUnitCode());
    }

    @Override
    public void remove(WarehouseRecord warehouse) throws UnknownWarehouseException {
        DbWarehouse dbWarehouse = findDbWarehouseByBusinessUnitCode(warehouse.businessUnitCode());
        delete(checkDbWarehouseExists(dbWarehouse));
    }

    @Override
    public WarehouseRecord findByBusinessUnitCode(String buCode) throws UnknownWarehouseException {
        DbWarehouse dbWarehouse = findDbWarehouseByBusinessUnitCode(buCode);
        return mapper.toWarehouse(checkDbWarehouseExists(dbWarehouse));
    }

    @Override
    public List<WarehouseRecord> findByLocation(String location) {
        return find("location = ?1 and archivedAt is null", location).stream().map(mapper::toWarehouse).toList();
    }

    @Override
    public WarehouseRecord findById(long id) {
        DbWarehouse dbWarehouse = find("id = ?1 and archivedAt is null", id).firstResult();
        checkDbWarehouseExists(dbWarehouse);
        return mapper.toWarehouse(dbWarehouse);
    }

    private DbWarehouse findDbWarehouseByBusinessUnitCode(String buCode) {
        return find("businessUnitCode = ?1 and archivedAt is null", buCode).firstResult();
    }

    private DbWarehouse checkDbWarehouseExists(DbWarehouse dbWarehouse) throws UnknownWarehouseException {
        return Optional.ofNullable(dbWarehouse).orElseThrow(UnknownWarehouseException::new);
    }

    private void checkDbWarehouseAbsent(DbWarehouse dbWarehouse) throws DuplicateWarehouseException {
        if (dbWarehouse != null) {
            throw new DuplicateWarehouseException();
        }
    }
}
