package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.stores.events.StoreOperation;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class StoreResourceTest {

    @InjectMock
    CdiStoreEventPublisher storeEventPublisher;

    @Inject
    StoreResource storeResource;

    // ---------------- GET ----------------
    @Test
    void givenStoresExist_whenGet_thenReturnList() {
        // given - nothing to mock for listAll (Panache returns DB list)

        // when
        List<Store> stores = storeResource.get();

        // then
        assertNotNull(stores);
    }

    @Test
    @Transactional
    void givenStoreExists_whenGetSingle_thenReturnStore() {
        // given
        Store store = new Store();
        store.name = "Test Store";
        Store.persist(store);

        // when
        Store result = storeResource.getSingle(store.id);

        // then
        assertEquals(result.name,"Test Store");
    }

    @Test
    void givenStoreDoesNotExist_whenGetSingle_thenThrow404() {
        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.getSingle(999L));
        assertEquals(404, ex.getResponse().getStatus());
    }

    // ---------------- CREATE ----------------
    @Test
    @Transactional
    void givenValidStore_whenCreate_thenPersistAndPublishEvent() {
        // given
        Store store = new Store();
        store.name = "New Store";
        store.quantityProductsInStock = 10;

        // when
        Response response = storeResource.create(store);

        // then
        assertEquals(201, response.getStatus());
        then(storeEventPublisher).should().storeChanged(store, StoreOperation.CREATE);
    }

    @Test
    void givenStoreWithId_whenCreate_thenThrow422() {
        // given
        Store store = new Store();
        store.id = 1L;
        store.name = "Invalid Store";

        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.create(store));
        assertEquals(422, ex.getResponse().getStatus());
    }

    // ---------------- UPDATE ----------------
    @Test
    @Transactional
    void givenExistingStore_whenUpdate_thenApplyChangesAndPublishEvent() {
        // given
        Store existing = new Store();
        existing.name = "Old Name";
        existing.quantityProductsInStock = 5;
        Store.persist(existing);

        Store updated = new Store();
        updated.name = "Updated Name";
        updated.quantityProductsInStock = 20;

        // when
        Store result = storeResource.update(existing.id, updated);

        // then
        assertEquals("Updated Name", result.name);
        assertEquals(20, result.quantityProductsInStock);
        then(storeEventPublisher).should().storeChanged(updated, StoreOperation.CREATE);
    }

    @Test
    void givenStoreDoesNotExist_whenUpdate_thenThrow404() {
        // given
        Store updated = new Store();
        updated.name = "Updated Name";

        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.update(999L, updated));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void givenStoreWithNullName_whenUpdate_thenThrow422() {
        // given
        Store updated = new Store();
        updated.name = null;

        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.update(1L, updated));
        assertEquals(422, ex.getResponse().getStatus());
    }

    // ---------------- PATCH ----------------
    @Test
    @Transactional
    void givenExistingStore_whenPatch_thenUpdateFieldsAndPublishEvent() {
        // given
        Store existing = new Store();
        existing.name = "Old Name";
        existing.quantityProductsInStock = 5;
        Store.persist(existing);

        Store patch = new Store();
        patch.name = "New Name";
        patch.quantityProductsInStock = 15;

        // when
        Store result = storeResource.patch(existing.id, patch);

        // then
        assertEquals("New Name", result.name);
        assertEquals(15, result.quantityProductsInStock);
        then(storeEventPublisher).should().storeChanged(patch, StoreOperation.CREATE);
    }

    @Test
    void givenNonExistentStore_whenPatch_thenThrow404() {
        // given
        Store patch = new Store();
        patch.name = "New Name";

        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.patch(999L, patch));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    void givenPatchWithNullName_whenPatch_thenThrow422() {
        // given
        Store patch = new Store();
        patch.name = null;

        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.patch(1L, patch));
        assertEquals(422, ex.getResponse().getStatus());
    }

    // ---------------- DELETE ----------------
    @Test
    @Transactional
    void givenExistingStore_whenDelete_thenRemoveIt() {
        // given
        Store store = new Store();
        store.name = "To Delete";
        Store.persist(store);

        // when
        Response response = storeResource.delete(store.id);

        // then
        assertEquals(204, response.getStatus());
    }

    @Test
    void givenStoreDoesNotExist_whenDelete_thenThrow404() {
        // when/then
        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> storeResource.delete(999L));
        assertEquals(404, ex.getResponse().getStatus());
    }
}