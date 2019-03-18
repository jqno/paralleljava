package nl.jqno.paralleljava.dependencyinjection;

import io.vavr.Function1;
import io.vavr.collection.HashMap;
import nl.jqno.paralleljava.app.endpoints.DefaultEndpoints;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.server.Heroku;
import nl.jqno.paralleljava.app.server.Server;
import nl.jqno.paralleljava.app.server.SparkServer;
import org.slf4j.LoggerFactory;

public class WiredApplication {

    private static final int DEFAULT_PORT = 4567;

    private final Function1<Class<?>, Logger> loggerFactory;
    private final Server server;

    public WiredApplication() {
        loggerFactory = c -> new Slf4jLogger(LoggerFactory.getLogger(c));
        server = createServer(loggerFactory);
    }

    public void run() {
        server.run();
    }

    private static Server createServer(Function1<Class<?>, Logger> loggerFactory) {
        int port = getPort();
        var endpoints = new DefaultEndpoints();
        return new SparkServer(endpoints, port, loggerFactory.apply(SparkServer.class));
    }

    private static int getPort() {
        var processBuilder = new ProcessBuilder();
        var environment = HashMap.ofAll(processBuilder.environment());
        var heroku = new Heroku(environment);
        var port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);
        return port;
    }
}
