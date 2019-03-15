package nl.jqno.paralleljava.server;

import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.server.SparkServer;
import nl.jqno.picotest.Test;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class SparkServerTest extends Test {

    private static final int PORT = 1337;

    public void server() {
        var endpoints = new Endpoints();
        var server = new SparkServer(endpoints, PORT, new Slf4jLogger(getClass()));

        test("hello world works", () -> {
            server.run();
            Spark.awaitInitialization();

            given()
                    .port(PORT)
                    .when()
                    .get("/hello")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Hello world"));

            Spark.stop();
        });
    }
}
