package nl.jqno.paralleljava.app.persistence.database;

import io.vavr.control.Try;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;

public class DefaultJdbi implements Jdbi {
    private final org.jdbi.v3.core.Jdbi jdbi;
    private final Logger logger;

    public DefaultJdbi(String jdbcUrl, TodoMapper todoMapper, LoggerFactory loggerFactory) {
        this.jdbi = org.jdbi.v3.core.Jdbi
                .create(jdbcUrl)
                .registerRowMapper(todoMapper);
        this.logger = loggerFactory.create(getClass());
    }

    public <X extends Exception> Try<Void> execute(HandleConsumer<X> consumer) {
        return Try.<Void>of(() -> {
            jdbi.useHandle(consumer);
            return null;
        }).onFailure(f -> logger.wakeMeUp("Failed to execute statement", f));
    }

    public <T, X extends Exception> Try<T> query(HandleCallback<T, X> callback) {
        return Try.of(() -> jdbi.withHandle(callback))
                .onFailure(f -> logger.wakeMeUp("Failed to execute query", f));
    }
}
