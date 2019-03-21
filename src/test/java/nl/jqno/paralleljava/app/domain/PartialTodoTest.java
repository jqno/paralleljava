package nl.jqno.paralleljava.app.domain;

import io.vavr.control.Option;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.paralleljava.app.TestData.SomeTodo;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartialTodoTest extends Test {

    public void partialTodo() {

        test("equals and hashCode", () -> {
            EqualsVerifier.forClass(PartialTodo.class)
                    .verify();
        });

        test("getters (Some)", () -> {
            assertThat(SomeTodo.PARTIAL_COMPLETE.id()).isEqualTo(Option.of(42));
            assertThat(SomeTodo.PARTIAL_COMPLETE.title()).isEqualTo("title");
            assertThat(SomeTodo.PARTIAL_COMPLETE.url()).isEqualTo(Option.of("http://www.example.com"));
            assertThat(SomeTodo.PARTIAL_COMPLETE.completed()).isEqualTo(Option.of(true));
            assertThat(SomeTodo.PARTIAL_COMPLETE.order()).isEqualTo(1337);
        });

        test("getters (None)", () -> {
            assertThat(SomeTodo.PARTIAL_POST.id()).isEqualTo(Option.none());
            assertThat(SomeTodo.PARTIAL_POST.title()).isEqualTo("title");
            assertThat(SomeTodo.PARTIAL_POST.url()).isEqualTo(Option.none());
            assertThat(SomeTodo.PARTIAL_POST.completed()).isEqualTo(Option.none());
            assertThat(SomeTodo.PARTIAL_POST.order()).isEqualTo(1337);
        });

        test("toString", () -> {
            assertThat(SomeTodo.PARTIAL_COMPLETE.toString())
                    .isEqualTo("PartialTodo: [id=Some(42), title=title, url=Some(http://www.example.com), completed=Some(true), order=1337]");
            assertThat(SomeTodo.PARTIAL_POST.toString())
                    .isEqualTo("PartialTodo: [id=None, title=title, url=None, completed=None, order=1337]");
        });
    }
}
