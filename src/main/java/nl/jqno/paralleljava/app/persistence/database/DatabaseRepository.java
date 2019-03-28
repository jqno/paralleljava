package nl.jqno.paralleljava.app.persistence.database;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.persistence.Repository;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

public class DatabaseRepository implements Repository {

    public static final String DEFAULT_JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    private final Logger logger;
    private final Jdbi jdbi;

    public DatabaseRepository(String jdbcUrl, LoggerFactory loggerFactory) {
        this.jdbi = Jdbi.create(jdbcUrl);
        this.jdbi.registerRowMapper(Todo.class, new TodoMapper());
        this.logger = loggerFactory.create(getClass());
        logger.forProduction("Using database " + jdbcUrl);
    }

    public Try<Void> initialize() {
        var sql = "CREATE TABLE todo (id VARCHAR(36) PRIMARY KEY, title VARCHAR, completed BOOLEAN, index INTEGER)";
        return execute(handle -> handle.execute(sql))
                .orElse(Try.success(null)); // If it fails, the table probably already exists.
    }

    public Try<Void> createTodo(Todo todo) {
        return execute(handle ->
                handle.createUpdate("INSERT INTO todo (id, title, completed, index) VALUES (?, ?, ?, ?)")
                        .bind(0, todo.id())
                        .bind(1, todo.title())
                        .bind(2, todo.completed())
                        .bind(3, todo.order())
                        .execute());
    }

    public Try<Option<Todo>> get(UUID id) {
        return null;
    }

    public Try<List<Todo>> getAllTodos() {
        return query(handle ->
                handle.createQuery("SELECT id, title, completed, index FROM todo")
                        .mapTo(Todo.class)
                        .collect(List.collector()));
    }

    public Try<Void> updateTodo(Todo todo) {
        return null;
    }

    public Try<Void> delete(UUID id) {
        return null;
    }

    public Try<Void> clearAllTodos() {
        return null;
    }

    private <X extends Exception> Try<Void> execute(HandleConsumer<X> consumer) {
        return Try.of(() -> {
            jdbi.useHandle(consumer);
            return null;
        });
    }

    private <T, X extends Exception> Try<T> query(HandleCallback<T, X> callback) {
        return Try.of(() -> jdbi.withHandle(callback));
    }
}
