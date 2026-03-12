package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.exception.UnknownLocationException;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@ApplicationScoped
public class LocationGateway implements LocationResolver {

    private static final Logger log = LoggerFactory.getLogger(LocationGateway.class);

    private static final Map<String, Location> locations = new HashMap<>();
    private static final Map<String, ReentrantLock> locationLocks = new HashMap<>();

    static {
        appendLocation("ZWOLLE-001", 1, 40);
        appendLocation("ZWOLLE-002", 2, 50);
        appendLocation("AMSTERDAM-001", 5, 100);
        appendLocation("AMSTERDAM-002", 3, 75);
        appendLocation("TILBURG-001", 1, 40);
        appendLocation("HELMOND-001", 1, 45);
        appendLocation("EINDHOVEN-001", 2, 70);
        appendLocation("VETSBY-001", 1, 90);
    }

    static void appendLocation(String identification, int maxNumberOfWarehouses, int maxCapacity) {
        locations.put(identification, new Location(identification, maxNumberOfWarehouses, maxCapacity));
    }

    @Override
    public Location resolveByIdentifier(String identifier) {
        return Optional.ofNullable(locations.get(identifier)).orElseThrow(UnknownLocationException::new);
    }

    @Override
    public Location resolveByIdentifierAndLock(String identifier) {
        Location location = Optional.ofNullable(locations.get(identifier)).orElseThrow(UnknownLocationException::new);
        locationLocks.computeIfAbsent(location.identification(), l -> new ReentrantLock()).lock();
        log.debug("Location {} locked");
        return location;
    }

    @Override
    public void unlock(String identifier) {
        Optional.ofNullable(locationLocks.get(identifier)).orElseThrow(() -> new RuntimeException("This thread does not hold lock on location: " + identifier)).unlock();
        log.debug("Location {} unlocked",identifier);
    }
}
