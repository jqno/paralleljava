package nl.jqno.paralleljava.app;

import nl.jqno.paralleljava.app.domain.Todo;

public class TestData {

    public static final Todo SOME_TODO =
            new Todo(42, "title", "http://www.example.com", true, 1337);

    public static final String SOME_SERIALIZED_TODO =
            "{\"id\":42,\"title\":\"title\",\"url\":\"http://www.example.com\",\"completed\":true,\"order\":1337}";
}
