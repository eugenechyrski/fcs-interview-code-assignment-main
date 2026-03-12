package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;

import com.fulfilment.application.monolith.stores.Store;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@QuarkusTest
class LegacyStoreNotifierTest {

    @Inject
    LegacyStoreNotifier legacyStoreNotifier;

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @Test
    void shouldCallCreateOnLegacySystemWhenStoreCreated() {
        // given
        Store store = mock(Store.class);
        StoreChangedEvent event = new StoreChangedEvent(store, StoreOperation.CREATE);

        // when
        legacyStoreNotifier.onStoreChanged(event);

        // then
        verify(legacyStoreManagerGateway).createStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never()).updateStoreOnLegacySystem(any());
    }

    @Test
    void shouldCallUpdateOnLegacySystemWhenStoreUpdated() {
        // given
        Store store = mock(Store.class);
        StoreChangedEvent event = new StoreChangedEvent(store, StoreOperation.UPDATE);

        // when
        legacyStoreNotifier.onStoreChanged(event);

        // then
        verify(legacyStoreManagerGateway).updateStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never()).createStoreOnLegacySystem(any());
    }

    @Test
    void shouldNotThrowWhenLegacyGatewayFails() {
        // given
        Store store = mock(Store.class);
        StoreChangedEvent event = new StoreChangedEvent(store, StoreOperation.CREATE);

        doThrow(new RuntimeException("Legacy failure"))
                .when(legacyStoreManagerGateway)
                .createStoreOnLegacySystem(store);

        // when / then
        assertDoesNotThrow(() -> legacyStoreNotifier.onStoreChanged(event));
    }
}