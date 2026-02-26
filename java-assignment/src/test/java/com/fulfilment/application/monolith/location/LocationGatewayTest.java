package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.exception.UnknownLocationException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class LocationGatewayTest {

    @Test
    public void testWhenResolveExistingLocationShouldReturn() throws UnknownLocationException {
        // given
        LocationGateway locationGateway = new LocationGateway();

        // when
        Location location = locationGateway.resolveByIdentifier("ZWOLLE-001");

        // then
        assertEquals(location.identification(), "ZWOLLE-001");
    }

    @Test
    public void testWhenResolveNonExistingLocationShouldThrowException() {
        // given
        LocationGateway locationGateway = new LocationGateway();

        // when + then

        assertThrows(UnknownLocationException.class, () -> {
            locationGateway.resolveByIdentifier("UNKNOWN");
        });
    }

    @Test
    public void testWhenResolveLocationAndLockShouldLockAndUnlock() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        LocationGateway locationGateway = new LocationGateway();



        ExecutorService executor = Executors.newSingleThreadExecutor();
        // when
        locationGateway.resolveByIdentifierAndLock("ZWOLLE-001");
        Future<Boolean> future = executor.submit(() -> {
            locationGateway.resolveByIdentifierAndLock("ZWOLLE-001");
            return true;
        });

        Thread.sleep(200);

        // then
        assertFalse(future.isDone());

        locationGateway.unlock("ZWOLLE-001");
        assertTrue(future.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testWhenResolveNullLocationShouldThrowException() {
        // given
        LocationGateway locationGateway = new LocationGateway();

        // when + then

        assertThrows(UnknownLocationException.class, () -> {
            locationGateway.resolveByIdentifier(null);
        });
    }
}
