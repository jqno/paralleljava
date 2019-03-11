package nl.jqno.paralleljava.internal;

import io.vavr.collection.HashMap;
import nl.jqno.paralleljava.endpoints.Endpoints;
import nl.jqno.paralleljava.endpoints.Route;

import static spark.Spark.get;
import static spark.Spark.port;

public class SparkServer {

    private Endpoints endpoints;
    private int port;

    public SparkServer(Endpoints endpoints, int port) {
        this.endpoints = endpoints;
        this.port = port;
    }

    public void run() {
        port(port);
        get("/hello", convert(endpoints.helloWorld()));
    }

    private spark.Route convert(Route route) {
        return (request, response) -> route.handle(HashMap.ofAll(request.params()));
    }
}
