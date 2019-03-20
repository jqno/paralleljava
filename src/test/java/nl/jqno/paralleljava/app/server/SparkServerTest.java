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

        test("CORS Access-Control-AllowOrigin header is included", this::corsRequestsHeader);
        test("OPTION request", this::corsOptionsRequest);

        test("GET works", this::getRequest);
        test("POST works", this::postRequest);
        test("DELETE works", this::deleteRequest);
    }

    private void corsRequestsHeader() {
        when
                .get("/todo")
                .then()
                .header("Access-Control-Allow-Origin", "*");

        when
                .post("/todo")
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

    private void getRequest() {
        when
                .get("/todo")
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledGet);
    }

    private void postRequest() {
        when
                .post("/todo")
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledPost);
    }

    private void deleteRequest() {
        when
                .delete("/todo")
                .then()
                .statusCode(200);
        assertSingleCall(underlying.calledDelete);
    }

    private void assertSingleCall(int calledEndpoint) {
        assertThat(calledEndpoint).isEqualTo(1);
        assertThat(underlying.calledTotal()).isEqualTo(1);
    }

    private static class StubEndpoints implements Endpoints {

        public int calledGet = 0;
        public int calledPost = 0;
        public int calledDelete = 0;

        public void clear() {
            calledGet = 0;
            calledPost = 0;
            calledDelete = 0;
        }

        public int calledTotal() {
            return calledGet + calledPost + calledDelete;
        }

        public Route get() {
            return stubbed(() -> calledGet += 1);
        }

        public Route post() {
            return stubbed(() -> calledPost += 1);
        }

        public Route delete() {
            return stubbed(() -> calledDelete += 1);
        }

        private Route stubbed(Runnable block) {
            return ignored -> {
                block.run();
                return "";
            };
        }
    }
}
