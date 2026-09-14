package edu.ifrs;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

// Para rodar esse teste o serviço catalog precisa estar rodando na porta 9080
@QuarkusTest
class LoanResourceTest {

    @Test
    void testNewLoanForUnknownBook() {
        given()
                .contentType("application/json")
                .body("{\"bookId\":999999,\"borrower\":\"Rafael\"}")
                .when().post("/loans")
                .then()
                .statusCode(404);
    }
}
