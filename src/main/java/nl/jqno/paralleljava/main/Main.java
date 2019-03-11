package nl.jqno.paralleljava.main;

import nl.jqno.paralleljava.endpoints.Endpoints;
import nl.jqno.paralleljava.internal.Heroku;
import nl.jqno.paralleljava.internal.SparkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final int DEFAULT_PORT = 4567;

    public static void main(String... args) {
        Integer port = getPort();

        var endpoints = new Endpoints();
        var server = new SparkServer(endpoints, port);

        server.run();
    }

    private static Integer getPort() {
        var processBuilder = new ProcessBuilder();
        var heroku = new Heroku(processBuilder);
        var port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);
        log.info("Starting on port " + port);
        return port;
    }
}
