package nl.jqno.paralleljava;

import io.vavr.collection.HashMap;
import nl.jqno.paralleljava.app.Runner;
import nl.jqno.paralleljava.app.controller.DefaultController;
import nl.jqno.paralleljava.app.environment.Environment;
import nl.jqno.paralleljava.app.environment.HerokuEnvironment;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.persistence.RandomIdGenerator;
import nl.jqno.paralleljava.app.persistence.database.DatabaseRepository;
import nl.jqno.paralleljava.app.persistence.database.DefaultJdbi;
import nl.jqno.paralleljava.app.persistence.database.TodoMapper;
import nl.jqno.paralleljava.app.serialization.GsonSerializer;
import nl.jqno.paralleljava.app.server.SparkServer;

public class Main {
    public static void main(String... args) {
        LoggerFactory loggerFactory = c -> new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(c));

        var processBuilder = new ProcessBuilder();
        var environmentMap = HashMap.ofAll(processBuilder.environment());
        var environment = new HerokuEnvironment(environmentMap);

        var fullUrl = environment.hostUrl().getOrElse(Environment.DEFAULT_URL) + Environment.ENDPOINT;
        var port = environment.port().getOrElse(Environment.DEFAULT_PORT);
        var jdbcUrl = environment.jdbcUrl().getOrElse(Environment.DEFAULT_JDBC_URL);

        var todoMapper = new TodoMapper(fullUrl);
        var jdbi = new DefaultJdbi(jdbcUrl, todoMapper, loggerFactory);
        var repository = new DatabaseRepository(jdbi);

        var idGenerator = new RandomIdGenerator();
        var serializer = GsonSerializer.create(loggerFactory);
        var controller = new DefaultController(fullUrl, repository, idGenerator, serializer, loggerFactory);
        var server = new SparkServer(Environment.ENDPOINT, port, controller, loggerFactory);

        var runner = new Runner(repository, server);
        runner.startup();
    }
}
