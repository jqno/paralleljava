package nl.jqno.paralleljava.app.serialization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vavr.CheckedFunction0;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.Logger;

import java.lang.reflect.Type;
import java.util.UUID;

public class GsonSerializer implements Serializer {
    private static final Type LIST_OF_TODO_TYPE = new TypeToken<List<Todo>>(){}.getType();

    private final Gson gson;
    private final Logger logger;

    public GsonSerializer(Gson gson, Logger logger) {
        this.gson = gson;
        this.logger = logger;
    }

    public String serializeTodo(Todo todo) {
        return gson.toJson(todo);
    }

    public Option<Todo> deserializeTodo(String json) {
        return attemptDeserialization(json, () -> gson.fromJson(json, Todo.class));
    }

    public String serializePartialTodo(PartialTodo todo) {
        return gson.toJson(todo);
    }

    public Option<PartialTodo> deserializePartialTodo(String json) {
        return attemptDeserialization(json, () -> gson.fromJson(json, PartialTodo.class));
    }

    public String serializeTodos(List<Todo> todos) {
        return gson.toJson(todos);
    }

    public List<Todo> deserializeTodos(String json) {
        return attemptDeserialization(json, () -> (List<Todo>)gson.fromJson(json, LIST_OF_TODO_TYPE)).getOrElse(List.empty());
    }

    public String serializeUuid(UUID id) {
        return gson.toJson(id);
    }

    public Option<UUID> deserializeUuid(String json) {
        return attemptDeserialization(json, () -> gson.fromJson(json, UUID.class));
    }

    private <T> Option<T> attemptDeserialization(String json, CheckedFunction0<T> f) {
        return Try.of(f)
                .onFailure(t -> logger.firstThingNextMorning("Failed to deserialize " + json, t))
                .toOption();
    }
}
