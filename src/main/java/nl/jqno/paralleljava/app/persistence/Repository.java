package nl.jqno.paralleljava.app.persistence;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public interface Repository {
    Try<Void> createTodo(Todo todo);
    Try<Option<Todo>> get(UUID id);
    Try<List<Todo>> getAllTodos();
    Try<Void> updateTodo(Todo todo);
    Try<Void> delete(UUID id);
    Try<Void> clearAllTodos();
}
