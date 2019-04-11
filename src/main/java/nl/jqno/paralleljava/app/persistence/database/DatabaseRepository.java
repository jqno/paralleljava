package nl.jqno.paralleljava.app.persistence.database;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.persistence.Repository;
import org.jdbi.v3.core.Handle;

import java.util.UUID;

public class DatabaseRepository implements Repository {

    private Jdbi jdbi;

    public DatabaseRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Try<Void> initialize() {
        var sql = "CREATE TABLE todo (id VARCHAR(36) PRIMARY KEY, title VARCHAR, completed BOOLEAN, index INTEGER)";
        return jdbi.execute(handle -> handle.execute(sql))
                .recoverWith(f -> {
                    if (f.getMessage() != null && f.getMessage().toLowerCase().contains("\"todo\" already exists")) {
                        return Try.success(null);
                    } else {
                        return Try.failure(f);
                    }
                });
    }

    public Try<Void> create(Todo todo) {
        return jdbi.execute(handle ->
                handle.createUpdate("INSERT INTO todo (id, title, completed, index) VALUES (:id, :title, :completed, :order)")
                        .bind("id", todo.id().toString())
                        .bind("title", todo.title())
                        .bind("completed", todo.completed())
                        .bind("order", todo.order())
                        .execute());
    }

    public Try<Option<Todo>> get(UUID id) {
        return jdbi.query(handle -> handleGet(handle, id));
    }

    public Try<List<Todo>> getAll() {
        return jdbi.query(handle ->
                handle.createQuery("SELECT id, title, completed, index FROM todo")
                        .mapTo(Todo.class)
                        .collect(List.collector()));
    }

    public Try<Void> update(Todo todo) {
        return jdbi.execute(handle -> handleUpdate(handle, todo));
    }

    public Try<Todo> update(UUID id, Function1<Todo, Todo> f) {
        return jdbi.query(handle -> {
            var option = handleGet(handle, id);
            if (option.isEmpty()) {
                throw new IllegalArgumentException("Can't find Todo with id " + id);
            }
            var oldTodo = option.get();
            var newTodo = f.apply(oldTodo).withId(oldTodo.id());
            handleUpdate(handle, newTodo);
            return newTodo;
        });
    }

    public Try<Void> delete(UUID id) {
        return jdbi.execute(handle ->
                handle.createUpdate("DELETE FROM todo WHERE id = :id")
                        .bind("id", id.toString())
                        .execute());
    }

    public Try<Void> deleteAll() {
        return jdbi.execute(handle -> handle.execute("DELETE FROM todo"));
    }

    private Option<Todo> handleGet(Handle handle, UUID id) {
        var o = handle.createQuery("SELECT id, title, completed, index FROM todo WHERE id = :id")
                .bind("id", id.toString())
                .mapTo(Todo.class)
                .findFirst();
        return Option.ofOptional(o);
    }

    private int handleUpdate(Handle handle, Todo todo) {
        return handle.createUpdate("UPDATE todo SET title = :title, completed = :completed, index = :order WHERE id = :id")
                .bind("title", todo.title())
                .bind("completed", todo.completed())
                .bind("order", todo.order())
                .bind("id", todo.id().toString())
                .execute();
    }
}
