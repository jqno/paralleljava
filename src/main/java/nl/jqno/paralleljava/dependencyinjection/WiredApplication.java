package nl.jqno.paralleljava.dependencyinjection;

import nl.jqno.paralleljava.app.endpoints.Endpoints;
import nl.jqno.paralleljava.app.server.Heroku;
import nl.jqno.paralleljava.Main;
import nl.jqno.paralleljava.app.server.Server;
import nl.jqno.paralleljava.app.server.SparkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WiredApplication {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final int DEFAULT_PORT = 4567;

    private final Server server;

    public WiredApplication() {
        server = createServer();
    }

    public void run() {
        server.run();
    }

    private static Server createServer() {
        int port = getPort();
        var endpoints = new Endpoints();
        return new SparkServer(endpoints, port);
    }

    private static int getPort() {
        var processBuilder = new ProcessBuilder();
        var heroku = new Heroku(processBuilder);
        var port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);
        log.info("Starting on port " + port);
        return port;
    }
}
