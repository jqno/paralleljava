package nl.jqno.paralleljava.app.persistence;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.Logger;

import java.util.ArrayList;
import java.util.UUID;

public class InMemoryRepository implements Repository {
    private static final java.util.List<Todo> todos = new ArrayList<>();

    private final Logger logger;

    public InMemoryRepository(Logger logger) {
        this.logger = logger;
    }

    public void createTodo(Todo todo) {
        logger.forProduction("Creating Todo " + todo);
        todos.add(todo);
    }

    public Option<Todo> get(UUID id) {
        return List.ofAll(todos)
                .find(t -> t.id().equals(id));
    }

    public List<Todo> getAllTodos() {
        return List.ofAll(todos);
    }

    public void clearAllTodos() {
        logger.forProduction("Clearing all Todos");
        todos.clear();
    }
}
