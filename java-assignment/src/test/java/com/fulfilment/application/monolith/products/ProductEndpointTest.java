package com.fulfilment.application.monolith.products;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class ProductEndpointTest {

    private static final String PATH = "product";

    @Test
    void testGetNonExistingProduct() {
        given()
                .when().get(PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void testCreateProduct() {
        String newProductJson = """
                {
                  "name": "New Product",
                  "description": "A new product",
                  "price": 10.0,
                  "stock": 5
                }
                """;

        given()
                .contentType("application/json")
                .body(newProductJson)
                .when().post(PATH)
                .then()
                .statusCode(201)
                .body(containsString("New Product"), containsString("A new product"));
    }

    @Test
    void testCreateProductWithIdShouldFail() {
        String invalidProductJson = """
                {
                  "id": 10,
                  "name": "Invalid Product"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidProductJson)
                .when().post(PATH)
                .then()
                .statusCode(422);
    }

    @Test
    void testUpdateProduct() {
        String updateJson = """
                {
                  "name": "Updated Product",
                  "description": "Updated description",
                  "price": 20.0,
                  "stock": 15
                }
                """;

        given()
                .contentType("application/json")
                .body(updateJson)
                .when().put(PATH + "/1")
                .then()
                .statusCode(200)
                .body(containsString("Updated Product"), containsString("Updated description"));
    }

    @Test
    void testUpdateNonExistingProduct() {
        String updateJson = """
                {
                  "name": "Does not exist"
                }
                """;

        given()
                .contentType("application/json")
                .body(updateJson)
                .when().put(PATH + "/999999")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateWithoutNameShouldFail() {
        String invalidUpdateJson = """
                {
                  "description": "No name"
                }
                """;

        given()
                .contentType("application/json")
                .body(invalidUpdateJson)
                .when().put(PATH + "/1")
                .then()
                .statusCode(422);
    }

    @Test
    void testDeleteProduct() {
        given()
                .when().delete(PATH + "/1")
                .then()
                .statusCode(204);
    }

    @Test
    void testDeleteNonExistingProduct() {
        given()
                .when().delete(PATH + "/999999")
                .then()
                .statusCode(404);
    }
}