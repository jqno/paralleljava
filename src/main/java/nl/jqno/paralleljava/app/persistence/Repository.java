package nl.jqno.paralleljava.app.persistence;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public interface Repository {
    void createTodo(Todo todo);
    Option<Todo> get(UUID id);
    List<Todo> getAllTodos();
    void clearAllTodos();
}
