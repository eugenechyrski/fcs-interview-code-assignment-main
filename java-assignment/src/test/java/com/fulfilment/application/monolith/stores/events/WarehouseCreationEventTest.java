package com.fulfilment.application.monolith.warehouses.domain.usecases.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseCreationEventTest {

    @Test
    void shouldBeEqualWhenLocationsAreSame() {
        WarehouseCreationEvent event1 = new WarehouseCreationEvent("AMSTERDAM-001");
        WarehouseCreationEvent event2 = new WarehouseCreationEvent("AMSTERDAM-001");

        assertEquals(event1, event2);
    }

    @Test
    void shouldNotBeEqualWhenLocationsAreDifferent() {
        WarehouseCreationEvent event1 = new WarehouseCreationEvent("AMSTERDAM-001");
        WarehouseCreationEvent event2 = new WarehouseCreationEvent("ZWOLLE-001");

        assertNotEquals(event1, event2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        WarehouseCreationEvent event = new WarehouseCreationEvent("AMSTERDAM-001");

        assertNotEquals(null, event);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        WarehouseCreationEvent event = new WarehouseCreationEvent("AMSTERDAM-001");

        assertNotEquals(event, "AMSTERDAM-001");
    }

    @Test
    void shouldBeEqualToItself() {
        WarehouseCreationEvent event = new WarehouseCreationEvent("AMSTERDAM-001");

        assertEquals(event, event);
    }
}