package nl.jqno.paralleljava;

import nl.jqno.paralleljava.dependencyinjection.Wiring;

public class Main {
    private static final int DEFAULT_PORT = 4567;
    private static final String DEFAULT_URL = "http://localhost";
    private static final String ENDPOINT = "/todo";
    private static final String INMEMORY_H2_JDBC_URL = "jdbc:h2:mem:test";

    public static void main(String... args) {
        var loggerFactory = Wiring.slf4jLoggerFactory();

        var environment = Wiring.herokuEnvironment();
        var fullUrl = environment.hostUrl().getOrElse(DEFAULT_URL) + ENDPOINT;
        var port = environment.port().getOrElse(DEFAULT_PORT);
        var jdbcUrl = environment.jdbcUrl().getOrElse(INMEMORY_H2_JDBC_URL);

        var repository = Wiring.databaseRepository(jdbcUrl, loggerFactory);
        var idGenerator = Wiring.randomIdGenerator();
        var controller = Wiring.defaultController(fullUrl, repository, idGenerator, loggerFactory);
        var server = Wiring.sparkServer(ENDPOINT, port, controller, loggerFactory);

        repository.initialize()
                .onSuccess(ignored -> server.run());
    }
}
