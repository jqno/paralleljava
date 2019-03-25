package nl.jqno.paralleljava.dependencyinjection;

import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.persistence.IdGenerator;
import nl.jqno.paralleljava.dependencyinjection.stubs.ConstantIdGenerator;
import nl.jqno.paralleljava.dependencyinjection.stubs.NopLogger;
import nl.jqno.paralleljava.dependencyinjection.stubs.StubController;
import nl.jqno.paralleljava.dependencyinjection.stubs.StubLogger;

import java.util.UUID;

public class TestWiring {
    private TestWiring() {
        // Don't instantiate
    }

    public static LoggerFactory nopLoggerFactory() {
        return c -> new NopLogger();
    }

    public static StubLogger stubLogger() {
        return new StubLogger();
    }

    public static IdGenerator constantIdGenerator(UUID id) {
        return new ConstantIdGenerator(id);
    }

    public static StubController stubController() {
        return new StubController();
    }
}
