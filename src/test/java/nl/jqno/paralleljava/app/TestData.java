package nl.jqno.paralleljava.app;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;

public class TestData {

    public static final Todo SOME_TODO =
            new Todo(42, "title", "http://www.example.com", true, 1337);

    public static final Todo ANOTHER_TODO =
            new Todo(47, "something", "http://www.todobackend.com", false, 1);

    public static final List<Todo> SOME_LIST_OF_TODOS = List.of(SOME_TODO, ANOTHER_TODO);

    public static final PartialTodo SOME_PARTIAL_TODO =
            new PartialTodo(Option.of(42), "title", Option.of("http://www.example.com"), Option.of(true), 1337);

    public static final PartialTodo SOME_POSTED_PARTIAL_TODO =
            new PartialTodo(Option.none(), "title", Option.none(), Option.none(), 1337);

    public static final String SOME_SERIALIZED_TODO =
            "{\"id\":42,\"title\":\"title\",\"url\":\"http://www.example.com\",\"completed\":true,\"order\":1337}";

    public static final String ANOTHER_SERIALIZED_TODO =
            "{\"id\":47,\"title\":\"something\",\"url\":\"http://www.todobackend.com\",\"completed\":false,\"order\":1}";

    public static final String SOME_SERIALIZED_LIST_OF_TODOS =
            "[" + SOME_SERIALIZED_TODO + "," + ANOTHER_SERIALIZED_TODO + "]";
}
