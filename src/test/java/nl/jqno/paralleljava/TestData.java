package nl.jqno.paralleljava;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.domain.PartialTodo;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public interface TestData {

    String URL_PREFIX = "http://localhost/blabla";

    interface SomeTodo {
        UUID ID = UUID.randomUUID();
        String URL = URL_PREFIX + "/" + ID.toString();
        Todo TODO = new Todo(ID, "title", URL, true, 1337);

        PartialTodo PARTIAL_COMPLETE = new PartialTodo(Option.of(ID), Option.of("title"), Option.of(URL), Option.of(true), Option.of(1337));
        PartialTodo PARTIAL_POST = new PartialTodo(Option.none(), Option.of("title"), Option.none(), Option.none(), Option.none());

        String SERIALIZED = "{\"id\":\"" + ID + "\",\"title\":\"title\",\"url\":\"" + URL + "\",\"completed\":true,\"order\":1337}";
        String SERIALIZED_PARTIAL_POST = "{\"title\":\"title\"}";
        String SERIALIZED_PARTIAL_POST_WITH_ORDER = "{\"title\":\"title\",\"order\":1337}";
    }

    interface AnotherTodo {
        UUID ID = UUID.randomUUID();
        String URL = URL_PREFIX + "/" + ID.toString();
        Todo TODO = new Todo(ID, "something", URL, false, 1);
        String SERIALIZED = "{\"id\":\"" + ID + "\",\"title\":\"something\",\"url\":\"" + URL + "\",\"completed\":false,\"order\":1}";
    }

    interface ListOfTodos {
        List<Todo> LIST = List.of(SomeTodo.TODO, AnotherTodo.TODO);
        String SERIALIZED = "[" + SomeTodo.SERIALIZED + "," + AnotherTodo.SERIALIZED + "]";
    }

    interface Invalid {
        String ID = "this is an invalid uuid";
        String JSON = "this is an invalid json document";
        String SERIALIZED_TODO_WITH_NO_TITLE = "{\"order\":1337}";
    }
}
