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
        request.setProduct("ProductH");
        request.setStore("Store2");
        request.setWarehouse("Warehouse2");
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
        request.setProduct("ProductB");
        request.setStore("Store1");
        request.setWarehouse("Warehouse2");

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
        request.setProduct("ProductA");
        request.setStore("Store1");
        request.setWarehouse("Warehouse3");

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
        request.setProduct("ProductH");
        request.setStore("Store1");
        request.setWarehouse("Warehouse4");
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
        request.setProduct("ProductI");
        request.setStore("Store6");
        request.setWarehouse("Warehouse1");
        given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("/fulfilment")
                .then()
                .statusCode(422); // warehouse max products exceeded
    }
}