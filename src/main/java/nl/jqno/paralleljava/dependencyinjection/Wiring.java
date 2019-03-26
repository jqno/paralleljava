package nl.jqno.paralleljava.dependencyinjection;

import com.google.gson.GsonBuilder;
import io.vavr.collection.HashMap;
import io.vavr.gson.VavrGson;
import nl.jqno.paralleljava.app.controller.Controller;
import nl.jqno.paralleljava.app.controller.DefaultController;
import nl.jqno.paralleljava.app.environment.Environment;
import nl.jqno.paralleljava.app.environment.HerokuEnvironment;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.persistence.IdGenerator;
import nl.jqno.paralleljava.app.persistence.inmemory.InMemoryRepository;
import nl.jqno.paralleljava.app.persistence.RandomIdGenerator;
import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.serialization.GsonSerializer;
import nl.jqno.paralleljava.app.serialization.Serializer;
import nl.jqno.paralleljava.app.server.Server;
import nl.jqno.paralleljava.app.server.SparkServer;

public class Wiring {

    private Wiring() {
        // Don't instantiate
    }

    public static Environment herokuEnvironment() {
        var processBuilder = new ProcessBuilder();
        var environment = HashMap.ofAll(processBuilder.environment());
        return new HerokuEnvironment(environment);
    }

    public static LoggerFactory slf4jLoggerFactory() {
        return c -> new Slf4jLogger(org.slf4j.LoggerFactory.getLogger(c));
    }

    public static Serializer gsonSerializer(LoggerFactory loggerFactory) {
        var gsonBuilder = new GsonBuilder();
        VavrGson.registerAll(gsonBuilder);
        return new GsonSerializer(gsonBuilder.create(), loggerFactory);
    }

    public static Repository inMemoryRepository(LoggerFactory loggerFactory) {
        return new InMemoryRepository(loggerFactory);
    }

    public static IdGenerator randomIdGenerator() {
        return new RandomIdGenerator();
    }

    public static Controller defaultController(String fullUrl, Repository repository, IdGenerator idGenerator, LoggerFactory loggerFactory) {
        return new DefaultController(fullUrl, repository, idGenerator, gsonSerializer(loggerFactory), loggerFactory);
    }

    public static Server sparkServer(String endpoint, int port, Controller controller, LoggerFactory loggerFactory) {
        return new SparkServer(endpoint, port, controller, loggerFactory);
    }
}
