package nl.jqno.paralleljava.app.serialization;

import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.Todo;

public interface Serializer {
    String serializeTodo(Todo todo);
    Option<Todo> deserializeTodo(String json);
}
