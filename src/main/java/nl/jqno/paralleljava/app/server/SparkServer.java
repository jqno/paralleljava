package nl.jqno.paralleljava.app.server;

import io.vavr.control.Option;
import nl.jqno.paralleljava.app.controller.Controller;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;

import static spark.Spark.*;

public class SparkServer implements Server {

    private final String endpoint;
    private final Controller controller;
    private final int port;
    private final Logger logger;

    public SparkServer(String endpoint, Controller controller, int port, LoggerFactory loggerFactory) {
        this.endpoint = endpoint;
        this.controller = controller;
        this.port = port;
        this.logger = loggerFactory.create(getClass());
    }

    public void run() {
        logger.forProduction("Starting on port " + port);

        port(port);
        enableCors();

        get(endpoint, (request, response) -> controller.get());
        get(endpoint + "/:id", (request, response) -> controller.get(request.params("id")));
        post(endpoint, (request, response) -> controller.post(request.body()));
        patch(endpoint + "/:id", (request, response) -> controller.patch(request.params("id"), request.body()));
        delete(endpoint, (request, response) -> controller.delete());
        delete(endpoint + "/:id", (request, response) -> controller.delete(request.params("id")));
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
}
