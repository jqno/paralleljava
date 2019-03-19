package nl.jqno.paralleljava.app.serialization;

import com.google.gson.Gson;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;

public class GsonSerializer implements Serializer {
    private final Gson gson;

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    public String serializeTodo(Todo todo) {
        return gson.toJson(todo);
    }

    public Option<Todo> deserializeTodo(String json) {
        return Try.of(() -> gson.fromJson(json, Todo.class)).toOption();
    }
}
