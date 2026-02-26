package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@QuarkusIntegrationTest
public class WarehouseEndpointIT {

    @Test
    public void testSimpleListWarehouses() {

        final String path = "warehouse";

        // List all, should have all 3 products the database has initially:
        given()
                .when()
                .get(path)
                .then()
                .statusCode(200)
                .body(containsString("MWH.001"), containsString("MWH.012"), containsString("MWH.023"));
    }

    @Test
    public void testSimpleCheckingArchivingWarehouses() {

        final String path = "warehouse";

        //List all, should have all 3 products the database has initially:
        given()
                .when()
                .get(path)
                .then()
                .statusCode(200)
                .body(
                        containsString("MWH.001"),
                        containsString("MWH.012"),
                        containsString("MWH.023"),
                        containsString("ZWOLLE-001"),
                        containsString("AMSTERDAM-001"),
                        containsString("TILBURG-001"));

        // Archive the ZWOLLE-001:
        given().when().delete(path + "/1").then().statusCode(204);

        // List all, ZWOLLE-001 should be missing now:
        given()
                .when()
                .get(path)
                .then()
                .statusCode(200)
                .body(
                        not(containsString("ZWOLLE-001")),
                        containsString("AMSTERDAM-001"),
                        containsString("TILBURG-001"));
    }

    @Test
    public void testArchiveNonExistingWarehouse() {
        given()
                .when()
                .delete("warehouse/999999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReplaceWarehouse() {

        final String path = "warehouse";

        String updatedJson = """
                {                  
                  "businessUnitCode": "MWH.012",
                  "location": "ZWOLLE-002",
                  "capacity": 50,
                  "stock": 5
                }
                """;

        given()
                .contentType("application/json")
                .body(updatedJson)
                .when()
                .post(path + "/MWH.012/replacement")
                .then()
                .statusCode(200)
                .body(containsString("50"),
                        containsString("5"));
    }

    @Test
    public void testCreateWarehouse() {

        final String path = "warehouse";

        String newWarehouseJson = """
                {
                  "businessUnitCode": "MWH.999",
                  "location": "AMSTERDAM-001",
                  "capacity": 10,
                  "stock": 5
                }
                """;

        given()
                .contentType("application/json")
                .body(newWarehouseJson)
                .when()
                .post(path)
                .then()
                .statusCode(200)
                .body(containsString("MWH.999"),
                        containsString("AMSTERDAM-001"));
    }

    @Test
    public void testGetWarehouseById() {

        given()
                .when()
                .get("warehouse/3")
                .then()
                .statusCode(200)
                .body(containsString("MWH.023"));
    }

    @Test
    public void testGetNonExistingWarehouse() {

        given()
                .when()
                .get("warehouse/999999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testReplaceWithMismatchedBusinessUnit() {

        String invalidJson = """
                {               
                  "businessUnitCode": "MWH.999",
                  "location": "ZWOLLE-001",
                  "capacity": 99,
                  "stock": 10
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidJson)
                .when()
                .post("warehouse/MWH.001/replacement")
                .then()
                .statusCode(422);
    }

    @Test
    public void testCreateWithInvalidLocation() {

        String json = """
                {
                  "businessUnitCode": "MWH.BAD",
                  "location": "UNKNOWN-LOCATION",
                  "capacity": 10,
                  "stock": 5
                }
                """;

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("warehouse")
                .then()
                .statusCode(404);
    }
}
