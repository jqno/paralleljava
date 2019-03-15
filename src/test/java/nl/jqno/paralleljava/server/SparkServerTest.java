package nl.jqno.paralleljava.server;

import junit.framework.TestCase;
import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.server.SparkServer;
import spark.Spark;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class SparkServerTest extends TestCase {

    private static final int PORT = 1337;

    public void testServer() {
        var endpoints = new Endpoints();
        var server = new SparkServer(endpoints, PORT, new Slf4jLogger(getClass()));

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
    }
}
