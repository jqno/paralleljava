package nl.jqno.paralleljava.app.server;

import io.restassured.specification.RequestSpecification;
import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.endpoints.Route;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SparkServerTest extends Test {

    private static final int PORT = 1337;
    private final RequestSpecification when = given().port(PORT).when();
    private StubEndpoints underlying;

    public void server() {
        underlying = new StubEndpoints();

        beforeAll(() -> {
            new SparkServer(underlying, PORT, NopLogger.INSTANCE).run();
            Spark.awaitInitialization();
        });

        beforeEach(() -> {
            underlying.clear();
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
                .statusCode(200);
        assertThat(underlying.calledHelloWorld).isEqualTo(1);
        assertThat(underlying.calledTotal()).isEqualTo(1);
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

    private static class StubEndpoints implements Endpoints {

        public int calledHelloWorld = 0;

        public void clear() {
            calledHelloWorld = 0;
        }

        public int calledTotal() {
            return calledHelloWorld;
        }

        public Route helloWorld() {
            return stubbed(() -> calledHelloWorld += 1);
        }

        private Route stubbed(Runnable block) {
            return ignored -> {
                block.run();
                return "";
            };
        }
    }
}