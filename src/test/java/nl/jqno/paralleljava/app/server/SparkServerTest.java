package nl.jqno.paralleljava.app.server;

import io.restassured.specification.RequestSpecification;
import nl.jqno.paralleljava.app.controller.Controller;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;
import spark.Spark;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SparkServerTest extends Test {

    private static final int PORT = 1337;
    private static final String ENDPOINT = "/todo";
    private final RequestSpecification when = given().port(PORT).when();
    private StubController underlying;

    public void server() {
        underlying = new StubController();

        beforeAll(() -> {
            new SparkServer(ENDPOINT, underlying, PORT, NopLogger.FACTORY).run();
            Spark.awaitInitialization();
        });

        beforeEach(() -> {
            underlying.clear();
        });

        afterAll(Spark::stop);

        test("CORS Access-Control-AllowOrigin header is included", this::corsRequestsHeader);
        test("OPTION request", this::corsOptionsRequest);

        test("GET works", this::getRequest);
        test("GET with id works", this::getWithIdRequest);
        test("POST works", this::postRequest);
        test("DELETE works", this::deleteRequest);
    }

    private void corsRequestsHeader() {
        when
                .get(ENDPOINT)
                .then()
                .header("Access-Control-Allow-Origin", "*");

        when
                .post(ENDPOINT)
                .then()
                .header("Access-Control-Allow-Origin", "*");
    }

    private void corsOptionsRequest() {
        var headers = "XXX";
        var methods = "YYY";
        when
                .header("Access-Control-Request-Headers", headers)
                .header("Access-Control-Request-Method", methods)
                .options(ENDPOINT)
                .then()
                .header("Access-Control-Allow-Headers", headers)
                .header("Access-Control-Allow-Methods", methods);
    }

    private void getRequest() {
        when
                .get(ENDPOINT)
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledGet);
    }

    private void getWithIdRequest() {
        when
                .get(ENDPOINT + "/some-id")
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledGetWithId);
    }

    private void postRequest() {
        when
                .post(ENDPOINT)
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledPost);
    }

    private void deleteRequest() {
        when
                .delete(ENDPOINT)
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledDelete);
    }

    private void assertSingleCall(int calledEndpoint) {
        assertThat(calledEndpoint).isEqualTo(1);
        assertThat(underlying.calledTotal()).isEqualTo(1);
    }

    private static class StubController implements Controller {

        public int calledGet = 0;
        public int calledGetWithId = 0;
        public int calledPost = 0;
        public int calledDelete = 0;

        public void clear() {
            calledGet = 0;
            calledGetWithId = 0;
            calledPost = 0;
            calledDelete = 0;
        }

        public int calledTotal() {
            return calledGet + calledGetWithId + calledPost + calledDelete;
        }

        public String get() {
            calledGet += 1;
            return "";
        }

        public String get(String json) {
            calledGetWithId += 1;
            return "";
        }

        public String post(String json) {
            calledPost += 1;
            return "";
        }

        public String delete() {
            calledDelete += 1;
            return "";
        }
    }
}
