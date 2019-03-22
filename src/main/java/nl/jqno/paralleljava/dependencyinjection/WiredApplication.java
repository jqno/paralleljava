package nl.jqno.paralleljava.dependencyinjection;

import com.google.gson.GsonBuilder;
import io.vavr.collection.HashMap;
import io.vavr.gson.VavrGson;
import nl.jqno.paralleljava.app.controller.DefaultController;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.persistence.InMemoryRepository;
import nl.jqno.paralleljava.app.persistence.RandomIdGenerator;
import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.serialization.GsonSerializer;
import nl.jqno.paralleljava.app.serialization.Serializer;
import nl.jqno.paralleljava.app.server.Heroku;
import nl.jqno.paralleljava.app.server.Server;
import nl.jqno.paralleljava.app.server.SparkServer;

public class WiredApplication {

    private static final int DEFAULT_PORT = 4567;
    private static final String DEFAULT_URL = "http://localhost";
    private static final String ENDPOINT = "/todo";

    private final LoggerFactory loggerFactory;
    private final Repository repository;
    private final Server server;

    public WiredApplication() {
        loggerFactory = c -> new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(c));
        repository = new InMemoryRepository(loggerFactory);
        server = createServer(repository, loggerFactory);
    }

    public void run() {
        server.run();
    }

    private static Server createServer(Repository repository, LoggerFactory loggerFactory) {
        var heroku = createHeroku();
        int port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);
        var fullUrl = heroku.getHostUrl().getOrElse(DEFAULT_URL) + ENDPOINT;
        var idGenerator = new RandomIdGenerator();
        var controller = new DefaultController(fullUrl, repository, idGenerator, defaultSerializer(loggerFactory), loggerFactory);
        return new SparkServer(ENDPOINT, controller, port, loggerFactory);
    }

    private static Heroku createHeroku() {
        var processBuilder = new ProcessBuilder();
        var environment = HashMap.ofAll(processBuilder.environment());
        return new Heroku(environment);
    }

    public static Serializer defaultSerializer(LoggerFactory loggerFactory) {
        var gsonBuilder = new GsonBuilder();
        VavrGson.registerAll(gsonBuilder);
        return new GsonSerializer(gsonBuilder.create(), loggerFactory);
    }
}
