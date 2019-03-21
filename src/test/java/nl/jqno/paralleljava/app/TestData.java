package nl.jqno.paralleljava.app;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;

public class TestData {

    public static class SomeTodo {
        public static final Todo TODO =
                new Todo(42, "title", "http://www.example.com", true, 1337);

        public static final PartialTodo PARTIAL_COMPLETE =
                new PartialTodo(Option.of(42), "title", Option.of("http://www.example.com"), Option.of(true), 1337);

        public static final PartialTodo PARTIAL_POST =
                new PartialTodo(Option.none(), "title", Option.none(), Option.none(), 1337);

        public static final String SERIALIZED =
                "{\"id\":42,\"title\":\"title\",\"url\":\"http://www.example.com\",\"completed\":true,\"order\":1337}";
    }

    public static class AnotherTodo {
        public static final Todo TODO =
                new Todo(47, "something", "http://www.todobackend.com", false, 1);

        public static final String SERIALIZED =
                "{\"id\":47,\"title\":\"something\",\"url\":\"http://www.todobackend.com\",\"completed\":false,\"order\":1}";
    }

    public static class ListOfTodos {
        public static final List<Todo> LIST =
                List.of(SomeTodo.TODO, AnotherTodo.TODO);

        public static final String SERIALIZED =
                "[" + SomeTodo.SERIALIZED + "," + AnotherTodo.SERIALIZED + "]";
    }
}
