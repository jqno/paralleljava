package nl.jqno.paralleljava.dependencyinjection;

import com.google.gson.GsonBuilder;
import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.gson.VavrGson;
import nl.jqno.paralleljava.app.endpoints.DefaultEndpoints;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.persistence.InMemoryRepository;
import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.serialization.GsonSerializer;
import nl.jqno.paralleljava.app.serialization.Serializer;
import nl.jqno.paralleljava.app.server.Heroku;
import nl.jqno.paralleljava.app.server.Server;
import nl.jqno.paralleljava.app.server.SparkServer;
import org.slf4j.LoggerFactory;

public class WiredApplication {

    private static final int DEFAULT_PORT = 4567;
    private static final String DEFAULT_URL = "http://localhost";
    private static final String ENDPOINT = "/todo";

    private final Function1<Class<?>, Logger> loggerFactory;
    private final Repository repository;
    private final Server server;

    public WiredApplication() {
        loggerFactory = c -> new Slf4jLogger(LoggerFactory.getLogger(c));
        repository = new InMemoryRepository(loggerFactory.apply(InMemoryRepository.class));
        server = createServer(repository, loggerFactory);
    }

    public void run() {
        server.run();
    }

    public static Serializer defaultSerializer(Logger logger) {
        var gsonBuilder = new GsonBuilder();
        VavrGson.registerAll(gsonBuilder);
        return new GsonSerializer(gsonBuilder.create(), logger);
    }

    private static Server createServer(Repository repository, Function1<Class<?>, Logger> loggerFactory) {
        var heroku = createHeroku();
        int port = heroku.getAssignedPort().getOrElse(DEFAULT_PORT);
        var fullUrl = heroku.getHostUrl().getOrElse(DEFAULT_URL) + ENDPOINT;
        var endpoints = new DefaultEndpoints(fullUrl, repository, defaultSerializer(loggerFactory.apply(GsonSerializer.class)), loggerFactory.apply(DefaultEndpoints.class));
        return new SparkServer(ENDPOINT, endpoints, port, loggerFactory.apply(SparkServer.class));
    }

    private static Heroku createHeroku() {
        var processBuilder = new ProcessBuilder();
        var environment = HashMap.ofAll(processBuilder.environment());
        return new Heroku(environment);
    }
}
