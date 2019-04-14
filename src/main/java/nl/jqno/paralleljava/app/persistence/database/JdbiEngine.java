package nl.jqno.paralleljava.app.persistence.database;

import io.vavr.control.Try;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;

public class JdbiEngine implements Engine {
    private final Jdbi jdbi;
    private final Logger logger;

    public JdbiEngine(String jdbcUrl, TodoMapper todoMapper, LoggerFactory loggerFactory) {
        this.jdbi = Jdbi
                .create(jdbcUrl)
                .registerRowMapper(todoMapper);
        this.logger = loggerFactory.create(getClass());
    }

    public <X extends Exception> Try<Void> execute(HandleConsumer<X> consumer) {
        return Try.<Void>of(() -> {
            jdbi.useHandle(h -> h.useTransaction(consumer));
            return null;
        }).onFailure(f -> logger.wakeMeUp("Failed to execute statement", f));
    }

    public <T, X extends Exception> Try<T> query(HandleCallback<T, X> callback) {
        return Try.of(() -> jdbi.withHandle(h -> h.inTransaction(callback)))
                .onFailure(f -> logger.wakeMeUp("Failed to execute query", f));
    }
}
