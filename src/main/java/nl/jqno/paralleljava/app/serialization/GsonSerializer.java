package nl.jqno.paralleljava.app.serialization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;

import java.lang.reflect.Type;

public class GsonSerializer implements Serializer {
    private static final Type LIST_OF_TODO_TYPE = new TypeToken<List<Todo>>(){}.getType();

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

    public String serializePartialTodo(PartialTodo todo) {
        return gson.toJson(todo);
    }

    public Option<PartialTodo> deserializePartialTodo(String json) {
        return Try.of(() -> gson.fromJson(json, PartialTodo.class)).toOption();
    }

    public String serializeTodos(List<Todo> todos) {
        return gson.toJson(todos);
    }

    public List<Todo> deserializeTodos(String json) {
        return Try.of(() -> (List<Todo>)gson.fromJson(json, LIST_OF_TODO_TYPE)).getOrElse(List.empty());
    }
}
