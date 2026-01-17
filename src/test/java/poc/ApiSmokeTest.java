package poc;

import io.restassured.RestAssured;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiSmokeTest {

    private static MockWebServer server;

    @BeforeAll
    static void setup() throws IOException {
        server = new MockWebServer();
        server.start();

        // Mock server base URL (example: http://127.0.0.1:XXXXX/)
        RestAssured.baseURI = server.url("/").toString();
    }

    @AfterAll
    static void teardown() throws IOException {
        server.shutdown();
    }

    @Test
    void get_users_should_return_page_2() {
        // Arrange: enqueue stubbed response
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                {
                  "page": 2,
                  "data": [
                    {"id": 7, "email": "test@example.com"}
                  ]
                }
                """));

        // Act + Assert
        given()
            .queryParam("page", 2)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("page", equalTo(2))
            .body("data", not(empty()))
            .body("data[0].id", equalTo(7));

        // Optional: verify request (good talking point in interviews)
        try {
            RecordedRequest req = server.takeRequest();
            Assertions.assertEquals("/users?page=2", req.getPath());
            Assertions.assertEquals("GET", req.getMethod());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void create_user_should_return_201_and_id_createdAt() {
        server.enqueue(new MockResponse()
                .setResponseCode(201)
                .addHeader("Content-Type", "application/json")
                .setBody("""
                {
                  "name": "michael",
                  "job": "qa",
                  "id": "123",
                  "createdAt": "2026-01-17T18:00:00Z"
                }
                """));

        String payload = """
        {
          "name": "michael",
          "job": "qa"
        }
        """;

        given()
            .contentType("application/json")
            .body(payload)
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .body("name", equalTo("michael"))
            .body("job", equalTo("qa"))
            .body("id", notNullValue())
            .body("createdAt", notNullValue());

        // Optional: verify request body (also a strong interview talking point)
        try {
            RecordedRequest req = server.takeRequest();
            Assertions.assertEquals("/users", req.getPath());
            Assertions.assertEquals("POST", req.getMethod());
            Assertions.assertTrue(req.getBody().readUtf8().contains("\"name\": \"michael\""));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
