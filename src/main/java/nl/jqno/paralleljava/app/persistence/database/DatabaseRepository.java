package nl.jqno.paralleljava.app.persistence.database;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.persistence.Repository;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;

import java.util.UUID;

public class DatabaseRepository implements Repository {

    private final Jdbi jdbi;

    public DatabaseRepository(String jdbcUrl) {
        this.jdbi = Jdbi.create(jdbcUrl);
    }

    public Try<Void> initialize() {
        var sql = "CREATE TABLE todo (id VARCHAR(36) PRIMARY KEY, title VARCHAR, completed BOOLEAN, index INTEGER)";
        return execute(handle -> handle.execute(sql));
    }

    public Try<Void> createTodo(Todo todo) {
        return null;
    }

    public Try<Option<Todo>> get(UUID id) {
        return null;
    }

    public Try<List<Todo>> getAllTodos() {
        return null;
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

    private <T, X extends Exception> Try<Void> execute(HandleCallback<T, X> callback) {
        return query(callback).map(ignored -> null);
    }

    private <T, X extends Exception> Try<T> query(HandleCallback<T, X> callback) {
        return Try.of(() -> jdbi.withHandle(callback));
    }
}
