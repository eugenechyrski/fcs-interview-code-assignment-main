package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.domain.models.WarehouseRecord;
import com.fulfilment.application.monolith.warehouses.domain.ports.*;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;
import java.util.Objects;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

    @Inject
    CreateWarehouseOperation createWarehouseOperation;

    @Inject
    ListWarehousesOperation listWarehouseOperation;

    @Inject
    ArchiveWarehouseOperation archiveWarehouseOperation;

    @Inject
    ReplaceWarehouseOperation replaceWarehouseOperation;
    @Inject
    GetWarehouseOperation getWarehouseOperation;


    @Override
    public List<Warehouse> listAllWarehousesUnits() {
        return listWarehouseOperation.list(false).stream().map(this::toWarehouseResponse).toList();
    }

    @Override
    public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
        if (!Objects.isNull(data.getId())) {
            throw new WebApplicationException("Invalid warehouse: id is provided", 422);
        }
        createWarehouseOperation.create(fromWarehouseRequest(data));
        return toWarehouseResponse(getWarehouseOperation.getActiveByUnitId(data.getBusinessUnitCode()));

    }

    @Override
    public Warehouse getAWarehouseUnitByID(@NotNull String id) {
        WarehouseRecord warehouseRecord = getWarehouseOperation.getActiveById(id);
        return toWarehouseResponse(warehouseRecord);
    }

    @Override
    public void archiveAWarehouseUnitByID(@NotNull String id) {
        archiveWarehouseOperation.archive(new WarehouseRecord(id, null, null, null, null, null, null));
    }

    @Override
    public Warehouse replaceTheCurrentActiveWarehouse(String businessUnitCode, @NotNull Warehouse data) {
        if (!Objects.equals(businessUnitCode, data.getBusinessUnitCode())) {
            throw new WebApplicationException("Invalid request parameters:businessUnitCode does not match warehouse data", 422);
        }
        if (Objects.nonNull(data.getId())) {
            throw new WebApplicationException("Invalid request parameters: id should not be provided", 422);
        }
        replaceWarehouseOperation.replace(fromWarehouseRequest(data));
        return toWarehouseResponse(getWarehouseOperation.getActiveByUnitId(data.getBusinessUnitCode()));
    }

    private Warehouse toWarehouseResponse(WarehouseRecord warehouse) {
        var response = new Warehouse();
        response.setBusinessUnitCode(warehouse.businessUnitCode());
        response.setLocation(warehouse.location());
        response.setCapacity(warehouse.capacity());
        response.setStock(warehouse.stock());
        response.setId(warehouse.id());
        return response;
    }

    private WarehouseRecord fromWarehouseRequest(Warehouse request) {
        return new WarehouseRecord(
                request.getId(),
                request.getBusinessUnitCode(),
                request.getLocation(),
                request.getCapacity(),
                request.getStock(),
                null,
                null
        );

    }
    // TODO: validate input warehouse request  - out of scope

}
