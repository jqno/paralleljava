package nl.jqno.paralleljava.app.persistence;

import io.vavr.collection.List;
import nl.jqno.paralleljava.app.domain.Todo;

public interface Repository {
    void createTodo(Todo todo);
    List<Todo> getAllTodos();
    void clearAllTodos();
}
