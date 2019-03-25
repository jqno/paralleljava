package nl.jqno.paralleljava.app.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.paralleljava.dependencyinjection.TestData.SomeTodo;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoTest extends Test {

    public void todo() {

        test("equals and hashCode", () -> {
            EqualsVerifier.forClass(Todo.class)
                    .verify();
        });

        test("getters", () -> {
            assertThat(SomeTodo.TODO.id()).isEqualTo(SomeTodo.ID);
            assertThat(SomeTodo.TODO.title()).isEqualTo("title");
            assertThat(SomeTodo.TODO.url()).isEqualTo("http://www.example.com");
            assertThat(SomeTodo.TODO.completed()).isEqualTo(true);
            assertThat(SomeTodo.TODO.order()).isEqualTo(1337);
        });

        test("toString", () -> {
            assertThat(SomeTodo.TODO.toString())
                    .isEqualTo("Todo: [id=" + SomeTodo.ID + ", title=title, url=http://www.example.com, completed=true, order=1337]");
        });

        test("withTitle", () -> {
            var actual = SomeTodo.TODO.withTitle("another title");
            assertThat(actual.title()).isEqualTo("another title");
            assertThat(actual.id()).isEqualTo(SomeTodo.ID);
            assertThat(actual.url()).isEqualTo(SomeTodo.TODO.url());
            assertThat(actual.completed()).isEqualTo(SomeTodo.TODO.completed());
            assertThat(actual.order()).isEqualTo(SomeTodo.TODO.order());
        });

        test("withCompleted", () -> {
            var actual = SomeTodo.TODO.withCompleted(!SomeTodo.TODO.completed());
            assertThat(actual.completed()).isEqualTo(!SomeTodo.TODO.completed());
            assertThat(actual.id()).isEqualTo(SomeTodo.ID);
            assertThat(actual.title()).isEqualTo(SomeTodo.TODO.title());
            assertThat(actual.url()).isEqualTo(SomeTodo.TODO.url());
            assertThat(actual.order()).isEqualTo(SomeTodo.TODO.order());
        });

        test("withOrder", () -> {
            var actual = SomeTodo.TODO.withOrder(86);
            assertThat(actual.order()).isEqualTo(86);
            assertThat(actual.id()).isEqualTo(SomeTodo.ID);
            assertThat(actual.title()).isEqualTo(SomeTodo.TODO.title());
            assertThat(actual.url()).isEqualTo(SomeTodo.TODO.url());
            assertThat(actual.completed()).isEqualTo(SomeTodo.TODO.completed());
        });
    }
}
