package nl.jqno.paralleljava;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public class TestData {

    public static final String URL_PREFIX = "http://localhost/blabla";

    public static class SomeTodo {
        public static final UUID ID = UUID.randomUUID();

        public static final String URL = URL_PREFIX + "/" + ID.toString();

        public static final Todo TODO =
                new Todo(ID, "title", URL, true, 1337);

        public static final PartialTodo PARTIAL_COMPLETE =
                new PartialTodo(Option.of(ID), Option.of("title"), Option.of(URL), Option.of(true), Option.of(1337));

        public static final PartialTodo PARTIAL_POST =
                new PartialTodo(Option.none(), Option.of("title"), Option.none(), Option.none(), Option.none());

        public static final String SERIALIZED =
                "{\"id\":\"" + ID + "\",\"title\":\"title\",\"url\":\"" + URL + "\",\"completed\":true,\"order\":1337}";

        public static final String SERIALIZED_PARTIAL_POST =
                "{\"title\":\"title\"}";

        public static final String SERIALIZED_PARTIAL_POST_WITH_ORDER =
                "{\"title\":\"title\",\"order\":1337}";
    }

    public static class AnotherTodo {
        public static final UUID ID = UUID.randomUUID();

        public static final String URL = URL_PREFIX + "/" + ID.toString();

        public static final Todo TODO =
                new Todo(ID, "something", URL, false, 1);

        public static final String SERIALIZED =
                "{\"id\":\"" + ID + "\",\"title\":\"something\",\"url\":\"" + URL + "\",\"completed\":false,\"order\":1}";
    }

    public static class ListOfTodos {
        public static final List<Todo> LIST =
                List.of(SomeTodo.TODO, AnotherTodo.TODO);

        public static final String SERIALIZED =
                "[" + SomeTodo.SERIALIZED + "," + AnotherTodo.SERIALIZED + "]";
    }

    public static class Invalid {
        public static final String ID = "this is an invalid uuid";
        public static final String JSON = "this is an invalid json document";
        public static final String SERIALIZED_TODO_WITH_NO_TITLE = "{\"order\":1337}";
    }
}
