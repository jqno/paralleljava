package nl.jqno.paralleljava.app.server;

import io.vavr.control.Option;
import nl.jqno.paralleljava.app.controller.Controller;
import nl.jqno.paralleljava.app.controller.Request;
import nl.jqno.paralleljava.app.controller.Route;
import nl.jqno.paralleljava.app.logging.Logger;

import static spark.Spark.*;

public class SparkServer implements Server {

    private final String endpoint;
    private final Controller controller;
    private final int port;
    private final Logger logger;

    public SparkServer(String endpoint, Controller controller, int port, Logger logger) {
        this.endpoint = endpoint;
        this.controller = controller;
        this.port = port;
        this.logger = logger;
    }

    public void run() {
        logger.forProduction("Starting on port " + port);

        port(port);
        enableCors();

        get(endpoint, convert(controller.get()));
        post(endpoint, convert(controller.post()));
        delete(endpoint, convert(controller.delete()));
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
