package nl.jqno.paralleljava.dependencyinjection;

import io.vavr.Function1;
import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.server.Heroku;
import nl.jqno.paralleljava.app.server.Server;
import nl.jqno.paralleljava.app.server.SparkServer;

public class WiredApplication {

    private static final int DEFAULT_PORT = 4567;

    private final Function1<Class<?>, Logger> loggerFactory;
    private final Server server;

    public WiredApplication() {
        loggerFactory = Slf4jLogger::new;
        server = createServer(loggerFactory);
    }

    public void run() {
        server.run();
    }

    private static Server createServer(Function1<Class<?>, Logger> loggerFactory) {
        int port = getPort();
        var endpoints = new Endpoints();
        return new SparkServer(endpoints, port, loggerFactory.apply(SparkServer.class));
    }

    private static int getPort() {
        var processBuilder = new ProcessBuilder();
        var heroku = new Heroku(processBuilder);
        var port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);
        return port;
    }
}
