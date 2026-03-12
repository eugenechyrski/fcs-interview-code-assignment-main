package com.fulfilment.application.monolith.fulfillment.adapters.database;

import com.fulfilment.application.monolith.fulfillment.domain.ports.WarehouseFulfilmentStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseFulfilmentRepository implements WarehouseFulfilmentStore, PanacheRepository<WarehouseFulfilment> {

    @Override
    public void save(WarehouseFulfilment wf) {
        persist(wf);
    }

    @Override
    public boolean existsByProductStoreWarehouse(String product, String store, String warehouse) {
        return count("product = ?1 and store = ?2 and warehouse = ?3", product, store, warehouse) > 0;
    }

    @Override
    public long countByProductAndStore(String product, String store) {
        return count("product = ?1 and store = ?2", product, store);
    }

    @Override
    public long countDistinctWarehousesByStore(String store) {
        return getEntityManager()
                .createQuery("SELECT COUNT(DISTINCT wf.warehouse) FROM warehouse_fulfilment wf WHERE wf.store = :store", Long.class)
                .setParameter("store", store)
                .getSingleResult();
    }

    @Override
    public long countByWarehouse(String warehouse) {
        return count("warehouse = ?1", warehouse);
    }
}
