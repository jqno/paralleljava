package nl.jqno.paralleljava.app.endpoints;

import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.serialization.Serializer;

public class DefaultEndpoints implements Endpoints {
    private final Repository repository;
    private final Serializer serializer;

    public DefaultEndpoints(Repository repository, Serializer serializer) {
        this.repository = repository;
        this.serializer = serializer;
    }

    public Route get() {
        return ignored -> serializer.serializeTodos(repository.getAllTodos());
    }

    public Route post() {
        return request -> {
            var json = request.body();
            var todo = serializer.deserializeTodo(json);
            todo.forEach(repository::createTodo);
            return json;
        };
    }

    public Route delete() {
        return ignored -> {
            repository.clearAllTodos();
            return "";
        };
    }
}
