package nl.jqno.paralleljava.app.persistence.database;

import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.environment.Environment;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.TestData;
import nl.jqno.paralleljava.TestData.AnotherTodo;
import nl.jqno.paralleljava.TestData.SomeTodo;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class DatabaseRepositoryTest extends Test {

    private static final String IN_MEMORY_DATABASE = Environment.DEFAULT_JDBC_URL;
    private static final LoggerFactory NOP_LOGGER = c -> new NopLogger();
    private final TodoMapper todoMapper = new TodoMapper(TestData.URL_PREFIX);

    public void initialization() {

        test("a table is created", () -> {
            var repo = new DatabaseRepository(IN_MEMORY_DATABASE, todoMapper, NOP_LOGGER);
            var result = repo.initialize();
            assertThat(result).isSuccess();
        });

        test("initializing twice is a no-op the second time", () -> {
            var repo = new DatabaseRepository(IN_MEMORY_DATABASE, todoMapper, NOP_LOGGER);
            assertThat(repo.initialize()).isSuccess();
            assertThat(repo.initialize()).isSuccess();
        });
    }

    public void repository() {
        var repo = new DatabaseRepository(IN_MEMORY_DATABASE, todoMapper, NOP_LOGGER);

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

    public void failures() {
        var failingMapper = new TodoMapper("") {
            public Todo map(ResultSet rs, StatementContext ctx) throws SQLException {
                throw new SQLException("Intentional failure");
            }
        };
        var repo = new DatabaseRepository(IN_MEMORY_DATABASE, failingMapper, NOP_LOGGER);

        beforeAll(() -> {
            repo.initialize();
        });

        test("Query failures cause failed results", () -> {
            repo.createTodo(SomeTodo.TODO);

            var result = repo.getAllTodos();
            assertThat(result).isFailure();
        });
    }
}
