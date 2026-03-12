package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class LegacyStoreManagerGatewayTest {

    private LegacyStoreManagerGateway gateway;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        gateway = new LegacyStoreManagerGateway();
        // Capture console output
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testCreateStoreOnLegacySystem() {
        Store store = new Store("MyStore");

        gateway.createStoreOnLegacySystem(store);

        String output = outContent.toString();

        assertTrue(output.contains("Temporary file created at:"), "Temp file creation message missing");
        assertTrue(output.contains("Data written to temporary file."), "Data written message missing");
        assertTrue(output.contains("Data read from temporary file:"), "Data read message missing");
        assertTrue(output.contains("Temporary file deleted."), "File deletion message missing");
        assertTrue(output.contains("name =MyStore"), "Store name missing in output");
    }

    @Test
    void testUpdateStoreOnLegacySystem() {
        Store store = new Store("AnotherStore");

        gateway.updateStoreOnLegacySystem(store);

        String output = outContent.toString();

        assertTrue(output.contains("Temporary file created at:"), "Temp file creation message missing");
        assertTrue(output.contains("Data written to temporary file."), "Data written message missing");
        assertTrue(output.contains("Data read from temporary file:"), "Data read message missing");
        assertTrue(output.contains("Temporary file deleted."), "File deletion message missing");
        assertTrue(output.contains("name =AnotherStore"), "Store name missing in output");
    }
}