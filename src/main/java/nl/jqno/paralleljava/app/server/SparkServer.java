package nl.jqno.paralleljava.app.server;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.endpoints.Request;
import nl.jqno.paralleljava.app.endpoints.Route;
import nl.jqno.paralleljava.app.logging.Logger;

import static spark.Spark.*;

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
        enableCors();

        get("/todo", convert(endpoints.get()));
        post("/todo", convert(endpoints.post()));
        delete("/todo", convert(endpoints.delete()));
    }

    private void enableCors() {
        options("/*", (request, response) -> {
            Option.of(request.headers("Access-Control-Request-Headers"))
                    .forEach(h -> response.header("Access-Control-Allow-Headers", h));
            Option.of(request.headers("Access-Control-Request-Method"))
                    .forEach(h -> response.header("Access-Control-Allow-Methods", h));
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });
    }

    private spark.Route convert(Route route) {
        return (request, response) -> {
            var req = new Request(request.body());
            return route.handle(req);
        };
    }
}
