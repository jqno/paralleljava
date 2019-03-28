package nl.jqno.paralleljava.app.persistence.inmemory;

import nl.jqno.paralleljava.dependencyinjection.TestData.AnotherTodo;
import nl.jqno.paralleljava.dependencyinjection.TestData.SomeTodo;
import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.paralleljava.dependencyinjection.Wiring;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class InMemoryRepositoryTest extends Test {

    public void repository() {
        var repo = Wiring.inMemoryRepository(TestWiring.nopLoggerFactory());

        beforeEach(() -> {
            repo.clearAllTodos();
        });

        test("initialize does nothing", () -> {
            repo.initialize();
        });

        test("create a todo", () -> {
            var result = repo.createTodo(SomeTodo.TODO);

            assertThat(result).isSuccess();
            assertThat(repo.getAllTodos()).hasValueSatisfying(l -> assertThat(l).contains(SomeTodo.TODO));
        });

        test("get a specific todo", () -> {
            repo.createTodo(SomeTodo.TODO);

            assertThat(repo.get(SomeTodo.ID)).hasValueSatisfying(o -> assertThat(o).contains(SomeTodo.TODO));
            assertThat(repo.get(UUID.randomUUID())).hasValueSatisfying(o -> assertThat(o).isEmpty());
        });

        test("update a specific todo at index 0", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            var result = repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).contains(expected);
        });

        test("update a specific todo at index 1", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(AnotherTodo.TODO);
            repo.createTodo(SomeTodo.TODO);

            var result = repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).contains(expected);
        });

        test("delete a specific todo at index 0", () -> {
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            var result = repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).isEmpty();
        });

        test("update a specific todo at index 1", () -> {
            repo.createTodo(AnotherTodo.TODO);
            repo.createTodo(SomeTodo.TODO);

            var result = repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).isEmpty();
        });

        test("clearing all todos", () -> {
            repo.createTodo(SomeTodo.TODO);

            var result = repo.clearAllTodos();

            assertThat(result).isSuccess();
            assertThat(repo.getAllTodos()).hasValueSatisfying(l -> assertThat(l).isEmpty());
        });
    }
}
