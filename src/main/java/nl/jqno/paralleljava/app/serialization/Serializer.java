package nl.jqno.paralleljava.app.serialization;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public interface Serializer {
    String serializeTodo(Todo todo);
    Option<Todo> deserializeTodo(String json);

    String serializePartialTodo(PartialTodo todo);
    Option<PartialTodo> deserializePartialTodo(String json);

    String serializeTodos(List<Todo> todos);
    List<Todo> deserializeTodos(String json);

    String serializeUuid(UUID uuid);
    Option<UUID> deserializeUuid(String json);
}
