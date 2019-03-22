package nl.jqno.paralleljava.app.persistence;

import io.vavr.control.Option;
import nl.jqno.paralleljava.app.TestData.AnotherTodo;
import nl.jqno.paralleljava.app.TestData.SomeTodo;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRepositoryTest extends Test {

    public void repository() {
        var repo = new InMemoryRepository(NopLogger.FACTORY);

        beforeEach(() -> {
            repo.clearAllTodos();
        });

        test("create a todo", () -> {
            repo.createTodo(SomeTodo.TODO);
            assertThat(repo.getAllTodos()).contains(SomeTodo.TODO);
        });

        test("get a specific todo", () -> {
            repo.createTodo(SomeTodo.TODO);

            assertThat(repo.get(SomeTodo.ID)).isEqualTo(Option.some(SomeTodo.TODO));
            assertThat(repo.get(UUID.randomUUID())).isEqualTo(Option.none());
        });

        test("update a specific todo at index 0", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID);
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("update a specific todo at index 1", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(AnotherTodo.TODO);
            repo.createTodo(SomeTodo.TODO);

            repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID);
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("delete a specific todo at index 0", () -> {
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID);
            assertThat(actual).isEqualTo(Option.none());
        });

        test("update a specific todo at index 1", () -> {
            repo.createTodo(AnotherTodo.TODO);
            repo.createTodo(SomeTodo.TODO);

            repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID);
            assertThat(actual).isEqualTo(Option.none());
        });

        test("clearing all todos", () -> {
            repo.createTodo(SomeTodo.TODO);
            repo.clearAllTodos();
            assertThat(repo.getAllTodos()).isEmpty();
        });
    }
}
