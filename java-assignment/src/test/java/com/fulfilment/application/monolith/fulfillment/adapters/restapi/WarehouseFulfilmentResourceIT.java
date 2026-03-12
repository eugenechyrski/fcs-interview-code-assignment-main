package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import com.warehouse.api.beans.WarehouseFulfilmentRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class WarehouseFulfilmentResourceIT {

    @Test
    void assignNewValidFulfilmentAndExpectContractinViolation() {
        WarehouseFulfilmentRequest request = new WarehouseFulfilmentRequest();
        request.setProduct(8);
        request.setStore(2);
        request.setWarehouse("MWH.012");
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(201);
    }

    @Test
    void rejectDuplicateAssignment() {
        WarehouseFulfilmentRequest request = new WarehouseFulfilmentRequest();
        request.setProduct(2);
        request.setStore(1);
        request.setWarehouse("MWH.012");

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(201); // API silently ignores duplicates
    }

    @Test
    void rejectProductWarehouseConstraintExceeded() {
        WarehouseFulfilmentRequest request = new WarehouseFulfilmentRequest();
        request.setProduct(1);
        request.setStore(1);
        request.setWarehouse("MWH.023");

        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(422); // product-store max warehouses exceeded
    }

    @Test
    void rejectStoreWarehouseConstraintExceeded() {
        WarehouseFulfilmentRequest request = new WarehouseFulfilmentRequest();
        request.setProduct(8);
        request.setStore(1);
        request.setWarehouse("MWH.024");
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(422); // store max warehouses exceeded
    }

    @Test
    void rejectWarehouseProductConstraintExceeded() {
        WarehouseFulfilmentRequest request = new WarehouseFulfilmentRequest();
        request.setProduct(9);
        request.setStore(6);
        request.setWarehouse("MWH.001");
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(422); // warehouse max products exceeded
    }
}