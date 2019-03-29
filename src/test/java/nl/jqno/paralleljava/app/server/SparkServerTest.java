package nl.jqno.paralleljava.app.server;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vavr.collection.List;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.paralleljava.app.controller.StubController;
import nl.jqno.picotest.Test;
import spark.Spark;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SparkServerTest extends Test {

    private static final int PORT = 1337;
    private static final String ENDPOINT = "/todo";
    private static final String ENDPOINT_WITH_ID = ENDPOINT + "/some-id";
    private final RequestSpecification when = given().port(PORT).when();
    private StubController underlying;
    private Server server;

    public void server() {
        underlying = new StubController();
        server = new SparkServer(ENDPOINT, PORT, underlying, c -> new NopLogger());

        beforeAll(() -> {
            server.run();
            Spark.awaitInitialization();
        });

        beforeEach(() -> {
            underlying.clear();
        });

        afterAll(Spark::stop);

        test("CORS Access-Control-AllowOrigin header is included",
                this::corsRequestsHeader);
        test("OPTION request",
                this::corsOptionsRequest);

        test("GET works",
                () -> checkEndpoint(() -> underlying.calledGet, () -> when.get(ENDPOINT)));
        test("GET with id works",
                () -> checkEndpoint(() -> underlying.calledGetWithId, () -> when.get(ENDPOINT_WITH_ID)));
        test("POST works",
                () -> checkEndpoint(() -> underlying.calledPost, () -> when.post(ENDPOINT)));
        test("PATCH with id works",
                () -> checkEndpoint(() -> underlying.calledPatchWithId, () -> when.patch(ENDPOINT_WITH_ID)));
        test("DELETE works",
                () -> checkEndpoint(() -> underlying.calledDelete, () -> when.delete(ENDPOINT)));
        test("DELETE with id works",
                () -> checkEndpoint(() -> underlying.calledDeleteWithId, () -> when.delete(ENDPOINT_WITH_ID)));
    }

    private void corsRequestsHeader() {
        var requests = List.of(
                when.get(ENDPOINT),
                when.get(ENDPOINT_WITH_ID),
                when.post(ENDPOINT),
                when.patch(ENDPOINT_WITH_ID),
                when.delete(ENDPOINT),
                when.delete(ENDPOINT_WITH_ID));
        requests.forEach(r -> r.then().header("Access-Control-Allow-Origin", "*"));
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

    private void checkEndpoint(IntSupplier calledEndpoint, Supplier<Response> r) {
        r.get().then().statusCode(200);
        assertSingleCall(calledEndpoint);

        underlying.clear();
        underlying.nextRequestFails4xx = true;
        r.get().then().statusCode(400);
        assertSingleCall(calledEndpoint);

        underlying.clear();
        underlying.nextRequestFails5xx = true;
        r.get().then().statusCode(500);
        assertSingleCall(calledEndpoint);
    }

    private void assertSingleCall(IntSupplier calledEndpoint) {
        assertThat(calledEndpoint.getAsInt()).isEqualTo(1);
        assertThat(underlying.calledTotal()).isEqualTo(1);
    }
}
