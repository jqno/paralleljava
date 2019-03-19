package nl.jqno.paralleljava.app.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.SOME_TODO;
import static org.assertj.core.api.Assertions.assertThat;

public class TodoTest extends Test {

    public void todo() {

        test("equals and hashCode", () -> {
            EqualsVerifier.forClass(Todo.class)
                    .verify();
        });

        test("getters", () -> {
            assertThat(SOME_TODO.id()).isEqualTo(42);
            assertThat(SOME_TODO.title()).isEqualTo("title");
            assertThat(SOME_TODO.url()).isEqualTo("http://www.example.com");
            assertThat(SOME_TODO.completed()).isEqualTo(true);
            assertThat(SOME_TODO.order()).isEqualTo(1337);
        });

        test("toString", () -> {
            assertThat(SOME_TODO.toString()).isEqualTo("Todo: [id=42, title=title, url=http://www.example.com, completed=true, order=1337]");
        });
    }
}
