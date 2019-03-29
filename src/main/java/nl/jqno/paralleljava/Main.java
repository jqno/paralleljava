package nl.jqno.paralleljava;

import nl.jqno.paralleljava.app.environment.Environment;
import nl.jqno.paralleljava.app.persistence.database.TodoMapper;
import nl.jqno.paralleljava.dependencyinjection.Wiring;

public class Main {
    public static void main(String... args) {
        var loggerFactory = Wiring.slf4jLoggerFactory();

        var environment = Wiring.herokuEnvironment();
        var fullUrl = environment.hostUrl().getOrElse(Environment.DEFAULT_URL) + Environment.ENDPOINT;
        var port = environment.port().getOrElse(Environment.DEFAULT_PORT);
        var jdbcUrl = environment.jdbcUrl().getOrElse(Environment.DEFAULT_JDBC_URL);

        var todoMapper = new TodoMapper(fullUrl);
        var repository = Wiring.databaseRepository(jdbcUrl, todoMapper, loggerFactory);
        var idGenerator = Wiring.randomIdGenerator();
        var controller = Wiring.defaultController(fullUrl, repository, idGenerator, loggerFactory);
        var server = Wiring.sparkServer(Environment.ENDPOINT, port, controller, loggerFactory);

        repository.initialize()
                .onSuccess(ignored -> server.run());
    }
}
