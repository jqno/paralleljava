package nl.jqno.paralleljava;

import nl.jqno.paralleljava.dependencyinjection.DefaultWiring;

public class Main {
    private static final int DEFAULT_PORT = 4567;
    private static final String DEFAULT_URL = "http://localhost";
    private static final String ENDPOINT = "/todo";

    public static void main(String... args) {
        var loggerFactory = DefaultWiring.slf4jLoggerFactory();

        var heroku = DefaultWiring.heroku();
        var fullUrl = heroku.getHostUrl().getOrElse(DEFAULT_URL) + ENDPOINT;
        var port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);

        var repository = DefaultWiring.inMemoryRepository(loggerFactory);
        var idGenerator = DefaultWiring.randomIdGenerator();
        var controller = DefaultWiring.defaultController(fullUrl, repository, idGenerator, loggerFactory);
        var server = DefaultWiring.sparkServer(ENDPOINT, port, controller, loggerFactory);

        server.run();
    }
}
