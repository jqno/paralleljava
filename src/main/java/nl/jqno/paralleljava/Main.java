package nl.jqno.paralleljava;

import nl.jqno.paralleljava.dependencyinjection.Wiring;

public class Main {
    private static final int DEFAULT_PORT = 4567;
    private static final String DEFAULT_URL = "http://localhost";
    private static final String ENDPOINT = "/todo";

    public static void main(String... args) {
        var loggerFactory = Wiring.slf4jLoggerFactory();

        var environment = Wiring.herokuEnvironment();
        var fullUrl = environment.hostUrl().getOrElse(DEFAULT_URL) + ENDPOINT;
        var port = environment.port().getOrElse(DEFAULT_PORT);

        var repository = Wiring.inMemoryRepository(loggerFactory);
        var idGenerator = Wiring.randomIdGenerator();
        var controller = Wiring.defaultController(fullUrl, repository, idGenerator, loggerFactory);
        var server = Wiring.sparkServer(ENDPOINT, port, controller, loggerFactory);

        server.run();
    }
}
