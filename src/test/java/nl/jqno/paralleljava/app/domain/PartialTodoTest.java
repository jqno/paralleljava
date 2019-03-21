package nl.jqno.paralleljava.app.domain;

import io.vavr.control.Option;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.SOME_PARTIAL_TODO;
import static nl.jqno.paralleljava.app.TestData.SOME_POSTED_PARTIAL_TODO;
import static org.assertj.core.api.Assertions.assertThat;

public class PartialTodoTest extends Test {

    public void partialTodo() {

        test("getters (Some)", () -> {
            assertThat(SOME_PARTIAL_TODO.id()).isEqualTo(Option.of(42));
            assertThat(SOME_PARTIAL_TODO.title()).isEqualTo("title");
            assertThat(SOME_PARTIAL_TODO.url()).isEqualTo(Option.of("http://www.example.com"));
            assertThat(SOME_PARTIAL_TODO.completed()).isEqualTo(Option.of(true));
            assertThat(SOME_PARTIAL_TODO.order()).isEqualTo(1337);
        });

        test("getters (None)", () -> {
            assertThat(SOME_POSTED_PARTIAL_TODO.id()).isEqualTo(Option.none());
            assertThat(SOME_POSTED_PARTIAL_TODO.title()).isEqualTo("title");
            assertThat(SOME_POSTED_PARTIAL_TODO.url()).isEqualTo(Option.none());
            assertThat(SOME_POSTED_PARTIAL_TODO.completed()).isEqualTo(Option.none());
            assertThat(SOME_POSTED_PARTIAL_TODO.order()).isEqualTo(1337);
        });

        test("toString", () -> {
            assertThat(SOME_PARTIAL_TODO.toString())
                    .isEqualTo("PartialTodo: [id=Some(42), title=title, url=Some(http://www.example.com), completed=Some(true), order=1337]");
            assertThat(SOME_POSTED_PARTIAL_TODO.toString())
                    .isEqualTo("PartialTodo: [id=None, title=title, url=None, completed=None, order=1337]");
        });
    }
}
