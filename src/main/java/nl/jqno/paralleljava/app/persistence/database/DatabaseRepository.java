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

/**
 * NOTE: this class is totally not thread-safe! It totally ignores database transactions.
 */
public class DatabaseRepository implements Repository {

    private final Logger logger;
    private final Jdbi jdbi;

    public DatabaseRepository(String jdbcUrl, TodoMapper todoMapper, LoggerFactory loggerFactory) {
        this.jdbi = Jdbi.create(jdbcUrl);
        this.jdbi.registerRowMapper(Todo.class, todoMapper);
        this.logger = loggerFactory.create(getClass());
        logger.forProduction("Using database " + jdbcUrl);
    }

    public Try<Void> initialize() {
        var sql = "CREATE TABLE todo (id VARCHAR(36) PRIMARY KEY, title VARCHAR, completed BOOLEAN, index INTEGER)";
        return execute(handle -> handle.execute(sql))
                .orElse(Try.success(null)); // If it fails, the table probably already exists.
    }

    public Try<Void> create(Todo todo) {
        return execute(handle ->
                handle.createUpdate("INSERT INTO todo (id, title, completed, index) VALUES (:id, :title, :completed, :order)")
                        .bind("id", todo.id().toString())
                        .bind("title", todo.title())
                        .bind("completed", todo.completed())
                        .bind("order", todo.order())
                        .execute());
    }

    public Try<Option<Todo>> get(UUID id) {
        return query(handle -> {
            var o = handle.createQuery("SELECT id, title, completed, index FROM todo WHERE id = :id")
                    .bind("id", id.toString())
                    .mapTo(Todo.class)
                    .findFirst();
            return Option.ofOptional(o);
        });
    }

    public Try<List<Todo>> getAll() {
        return query(handle ->
                handle.createQuery("SELECT id, title, completed, index FROM todo")
                        .mapTo(Todo.class)
                        .collect(List.collector()));
    }

    public Try<Void> update(Todo todo) {
        return execute(handle ->
                handle.createUpdate("UPDATE todo SET title = :title, completed = :completed, index = :order WHERE id = :id")
                        .bind("title", todo.title())
                        .bind("completed", todo.completed())
                        .bind("order", todo.order())
                        .bind("id", todo.id().toString())
                        .execute());
    }

    public Try<Void> delete(UUID id) {
        return execute(handle ->
                handle.createUpdate("DELETE FROM todo WHERE id = :id")
                        .bind("id", id.toString())
                        .execute());
    }

    public Try<Void> deleteAll() {
        return execute(handle -> handle.execute("DELETE FROM todo"));
    }

    private <X extends Exception> Try<Void> execute(HandleConsumer<X> consumer) {
        return Try.<Void>of(() -> {
            jdbi.useHandle(consumer);
            return null;
        }).onFailure(f -> logger.wakeMeUp("Failed to execute statement", f));
    }

    private <T, X extends Exception> Try<T> query(HandleCallback<T, X> callback) {
        return Try.of(() -> jdbi.withHandle(callback))
                .onFailure(f -> logger.wakeMeUp("Failed to execute query", f));
    }
}
