package edu.ifrs;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class CatalogResourceTest {

    @Test
    void testAddBook() {
        given()
                .contentType("application/json")
                .body("{\"title\":\"Dom Casmurro\",\"author\":\"Machado de Assis\"}")
                .when().post("/books")
                .then()
                .statusCode(201)
                .body("title", is("Dom Casmurro"));
    }

    @Test
    void testGetBookNotFound() {
        given()
                .when().get("/books/9999")
                .then()
                .statusCode(404);
    }
}
