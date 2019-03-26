package nl.jqno.paralleljava.app.persistence.inmemory;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.persistence.Repository;

import java.util.ArrayList;
import java.util.UUID;

/**
 * NOTE: this class is totally not thread-safe!
 */
public class InMemoryRepository implements Repository {
    private static final java.util.List<Todo> todos = new ArrayList<>();
    private static final Try<Void> SUCCESS = Try.success(null);

    private final Logger logger;

    public InMemoryRepository(LoggerFactory loggerFactory) {
        this.logger = loggerFactory.create(getClass());
    }

    public Try<Void> createTodo(Todo todo) {
        logger.forProduction("Creating Todo " + todo);
        todos.add(todo);
        return SUCCESS;
    }

    public Try<Option<Todo>> get(UUID id) {
        var result = List.ofAll(todos)
                .find(t -> t.id().equals(id));
        return Try.success(result);
    }

    public Try<List<Todo>> getAllTodos() {
        return Try.success(List.ofAll(todos));
    }

    public Try<Void> updateTodo(Todo todo) {
        var index = List.ofAll(todos)
                .map(Todo::id)
                .indexOf(todo.id());
        todos.remove(index);
        todos.add(index, todo);
        return SUCCESS;
    }

    public Try<Void> delete(UUID id) {
        var index = List.ofAll(todos)
                .map(Todo::id)
                .indexOf(id);
        todos.remove(index);
        return SUCCESS;
    }

    public Try<Void> clearAllTodos() {
        logger.forProduction("Clearing all Todos");
        todos.clear();
        return SUCCESS;
    }
}
