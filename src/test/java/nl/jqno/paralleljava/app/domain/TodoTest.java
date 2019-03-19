package nl.jqno.paralleljava.app.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoTest extends Test {

    public void todo() {

        test("equals and hashCode", () -> {
            EqualsVerifier.forClass(Todo.class)
                    .verify();
        });

        test("getters", () -> {
            var todo = new Todo(42, "title", "http://www.example.com", true, 1337);
            assertThat(todo.id()).isEqualTo(42);
            assertThat(todo.title()).isEqualTo("title");
            assertThat(todo.url()).isEqualTo("http://www.example.com");
            assertThat(todo.completed()).isEqualTo(true);
            assertThat(todo.order()).isEqualTo(1337);
        });
    }
}
