package nl.jqno.paralleljava.app.persistence.database;

import nl.jqno.paralleljava.dependencyinjection.TestData;
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

    public void placeholders() {
        var repo = Wiring.databaseRepository(DatabaseRepository.DEFAULT_JDBC_URL, todoMapper, TestWiring.nopLoggerFactory());

        test("createTodo placeholder", () -> {
            repo.createTodo(null);
        });
        test("get placeholder", () -> {
            repo.get(UUID.randomUUID());
        });
        test("getAllTodos placeholder", () -> {
            repo.getAllTodos();
        });
        test("updateTodo placeholder", () -> {
            repo.updateTodo(null);
        });
        test("delete placeholder", () -> {
            repo.delete(null);
        });
        test("clearAllTodos placeholder", () -> {
            repo.clearAllTodos();
        });
    }

    public void repository() {
        var repo = Wiring.databaseRepository(DatabaseRepository.DEFAULT_JDBC_URL, todoMapper, TestWiring.nopLoggerFactory());

        beforeAll(() -> {
            assertThat(repo.initialize()).isSuccess();
        });

        test("create a todo", () -> {
            var result = repo.createTodo(TestData.SomeTodo.TODO);

            assertThat(result).isSuccess();
            assertThat(repo.getAllTodos()).hasValueSatisfying(l -> l.contains(TestData.SomeTodo.TODO));
        });

        test("get a specific todo", () -> {
            repo.createTodo(TestData.SomeTodo.TODO);

            assertThat(repo.get(TestData.SomeTodo.ID)).hasValueSatisfying(o -> assertThat(o).contains(TestData.SomeTodo.TODO));
            assertThat(repo.get(UUID.randomUUID())).hasValueSatisfying(o -> assertThat(o).isEmpty());
        });
    }
}
