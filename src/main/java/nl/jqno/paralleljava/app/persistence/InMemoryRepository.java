package nl.jqno.paralleljava.app.persistence;

import io.vavr.collection.List;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.ArrayList;

public class InMemoryRepository implements Repository {

    private static final java.util.List<Todo> todos = new ArrayList<>();

    public void createTodo(Todo todo) {
        todos.add(todo);
    }

    public List<Todo> getAllTodos() {
        return List.ofAll(todos);
    }

    public void clearAllTodos() {
        todos.clear();
    }
}
