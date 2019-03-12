package nl.jqno.paralleljava.app.server;

import io.vavr.collection.HashMap;
import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.endpoints.Route;
import nl.jqno.paralleljava.app.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.port;

public class SparkServer implements Server {

    private final Endpoints endpoints;
    private final int port;
    private final Logger logger;

    public SparkServer(Endpoints endpoints, int port, Logger logger) {
        this.endpoints = endpoints;
        this.port = port;
        this.logger = logger;
    }

    public void run() {
        logger.forProduction("Starting on port " + port);
        port(port);
        get("/hello", convert(endpoints.helloWorld()));
    }

    private spark.Route convert(Route route) {
        return (request, response) -> route.handle(HashMap.ofAll(request.params()));
    }
}
