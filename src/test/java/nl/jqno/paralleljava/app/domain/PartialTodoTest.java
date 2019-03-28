package nl.jqno.paralleljava.app.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.paralleljava.dependencyinjection.TestData.SomeTodo;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class PartialTodoTest extends Test {

    public void partialTodo() {

        test("equals and hashCode", () -> {
            EqualsVerifier.forClass(PartialTodo.class)
                    .verify();
        });

        test("getters (Some)", () -> {
            assertThat(SomeTodo.PARTIAL_COMPLETE.id()).contains(SomeTodo.ID);
            assertThat(SomeTodo.PARTIAL_COMPLETE.title()).contains("title");
            assertThat(SomeTodo.PARTIAL_COMPLETE.url()).contains("http://www.example.com");
            assertThat(SomeTodo.PARTIAL_COMPLETE.completed()).contains(true);
            assertThat(SomeTodo.PARTIAL_COMPLETE.order()).contains(1337);
        });

        test("getters (None)", () -> {
            assertThat(SomeTodo.PARTIAL_POST.id()).isEmpty();
            assertThat(SomeTodo.PARTIAL_POST.title()).contains("title");
            assertThat(SomeTodo.PARTIAL_POST.url()).isEmpty();
            assertThat(SomeTodo.PARTIAL_POST.completed()).isEmpty();
            assertThat(SomeTodo.PARTIAL_POST.order()).isEmpty();
        });

        test("toString", () -> {
            assertThat(SomeTodo.PARTIAL_COMPLETE.toString())
                    .isEqualTo("PartialTodo: [id=Some(" + SomeTodo.ID + "), title=Some(title), url=Some(http://www.example.com), completed=Some(true), order=Some(1337)]");
            assertThat(SomeTodo.PARTIAL_POST.toString())
                    .isEqualTo("PartialTodo: [id=None, title=Some(title), url=None, completed=None, order=None]");
        });
    }
}
