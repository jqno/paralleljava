package nl.jqno.paralleljava.app.persistence.inmemory;

import io.vavr.control.Option;
import nl.jqno.paralleljava.dependencyinjection.TestData.AnotherTodo;
import nl.jqno.paralleljava.dependencyinjection.TestData.SomeTodo;
import nl.jqno.paralleljava.dependencyinjection.Wiring;
import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRepositoryTest extends Test {

    public void repository() {
        var repo = Wiring.inMemoryRepository(TestWiring.nopLoggerFactory());

        beforeEach(() -> {
            repo.clearAllTodos();
        });

        test("create a todo", () -> {
            var result = repo.createTodo(SomeTodo.TODO);

            assertThat(result.isSuccess()).isTrue();
            assertThat(repo.getAllTodos().get()).contains(SomeTodo.TODO);
        });

        test("get a specific todo", () -> {
            repo.createTodo(SomeTodo.TODO);

            assertThat(repo.get(SomeTodo.ID).get()).isEqualTo(Option.some(SomeTodo.TODO));
            assertThat(repo.get(UUID.randomUUID()).get()).isEqualTo(Option.none());
        });

        test("update a specific todo at index 0", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            var result = repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result.isSuccess()).isTrue();
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("update a specific todo at index 1", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(AnotherTodo.TODO);
            repo.createTodo(SomeTodo.TODO);

            var result = repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result.isSuccess()).isTrue();
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("delete a specific todo at index 0", () -> {
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            var result = repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result.isSuccess()).isTrue();
            assertThat(actual).isEqualTo(Option.none());
        });

        test("update a specific todo at index 1", () -> {
            repo.createTodo(AnotherTodo.TODO);
            repo.createTodo(SomeTodo.TODO);

            var result = repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result.isSuccess()).isTrue();
            assertThat(actual).isEqualTo(Option.none());
        });

        test("clearing all todos", () -> {
            repo.createTodo(SomeTodo.TODO);

            var result = repo.clearAllTodos();

            assertThat(result.isSuccess()).isTrue();
            assertThat(repo.getAllTodos().get()).isEmpty();
        });
    }
}
