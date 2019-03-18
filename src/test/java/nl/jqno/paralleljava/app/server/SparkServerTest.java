package nl.jqno.paralleljava.app.server;

import io.restassured.specification.RequestSpecification;
import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class SparkServerTest extends Test {

    private static final int PORT = 1337;
    private final RequestSpecification when = given().port(PORT).when();

    public void server() {
        var endpoints = new Endpoints();
        var server = new SparkServer(endpoints, PORT, NopLogger.INSTANCE);

        beforeAll(() -> {
            server.run();
            Spark.awaitInitialization();
        });

        afterAll(Spark::stop);

        test("hello world works", this::helloWorldWorks);
        test("CORS Access-Control-AllowOrigin header is included", this::corsRequestsHeader);
        test("OPTION request", this::corsOptionsRequest);
    }

    private void helloWorldWorks() {
        when
                .get("/hello")
                .then()
                .statusCode(200)
                .body(equalTo("Hello world"));
    }

    private void corsRequestsHeader() {
        when
                .get("/hello")
                .then()
                .header("Access-Control-Allow-Origin", "*");
    }

    private void corsOptionsRequest() {
        var headers = "XXX";
        var methods = "YYY";
        when
                .header("Access-Control-Request-Headers", headers)
                .header("Access-Control-Request-Method", methods)
                .options()
                .then()
                .header("Access-Control-Allow-Headers", headers)
                .header("Access-Control-Allow-Methods", methods);
    }
}
