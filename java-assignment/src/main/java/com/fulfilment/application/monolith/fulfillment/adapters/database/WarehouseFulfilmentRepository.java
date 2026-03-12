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
    public boolean existsByProductStoreWarehouse(long productId, long storeId, String warehouse) {
        return count("productId = ?1 and storeId = ?2 and warehouse = ?3", productId, storeId, warehouse) > 0;
    }

    @Override
    public long countByProductAndStore(long productId, long storeId) {
        return count("productId = ?1 and storeId = ?2", productId, storeId);
    }

    @Override
    public long countDistinctWarehousesByStore(long storeId) {
        return getEntityManager()
                .createQuery("SELECT COUNT(DISTINCT wf.warehouse) FROM warehouse_fulfilment wf WHERE wf.storeId = :storeId", Long.class)
                .setParameter("storeId", storeId)
                .getSingleResult();
    }

    @Override
    public long countByWarehouse(String warehouse) {
        return count("warehouse = ?1", warehouse);
    }
}
