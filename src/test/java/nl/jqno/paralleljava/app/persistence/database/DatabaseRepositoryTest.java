package nl.jqno.paralleljava.app.persistence.database;

import nl.jqno.paralleljava.dependencyinjection.TestData;
import nl.jqno.paralleljava.dependencyinjection.TestData.AnotherTodo;
import nl.jqno.paralleljava.dependencyinjection.TestData.SomeTodo;
import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.paralleljava.dependencyinjection.Wiring;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class DatabaseRepositoryTest extends Test {

    private final TodoMapper todoMapper = new TodoMapper(TestData.URL_PREFIX);

    public void initialization() {

        test("a table is created", () -> {
            var repo = Wiring.databaseRepository(DatabaseRepository.DEFAULT_JDBC_URL, todoMapper, TestWiring.nopLoggerFactory());
            var result = repo.initialize();
            assertThat(result).isSuccess();
        });

        test("initializing twice is a no-op the second time", () -> {
            var repo = Wiring.databaseRepository(DatabaseRepository.DEFAULT_JDBC_URL, todoMapper, TestWiring.nopLoggerFactory());
            assertThat(repo.initialize()).isSuccess();
            assertThat(repo.initialize()).isSuccess();
        });
    }

    public void repository() {
        var repo = Wiring.databaseRepository(DatabaseRepository.DEFAULT_JDBC_URL, todoMapper, TestWiring.nopLoggerFactory());

        beforeAll(() -> {
            assertThat(repo.initialize()).isSuccess();
            assertThat(repo.clearAllTodos()).isSuccess();
        });

        test("create a todo", () -> {
            var result = repo.createTodo(SomeTodo.TODO);

            assertThat(result).isSuccess();
            assertThat(repo.getAllTodos()).hasValueSatisfying(l -> l.contains(SomeTodo.TODO));
        });

        test("get a specific todo", () -> {
            repo.createTodo(SomeTodo.TODO);

            assertThat(repo.get(SomeTodo.ID)).hasValueSatisfying(o -> assertThat(o).contains(SomeTodo.TODO));
            assertThat(repo.get(UUID.randomUUID())).hasValueSatisfying(o -> assertThat(o).isEmpty());
        });

        test("update a specific todo", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

            var result = repo.updateTodo(expected);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).contains(expected);
        });

        test("delete a specific todo", () -> {
            repo.createTodo(SomeTodo.TODO);
            repo.createTodo(AnotherTodo.TODO);

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
