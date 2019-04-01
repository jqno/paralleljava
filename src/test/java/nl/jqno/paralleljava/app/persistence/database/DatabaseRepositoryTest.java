package nl.jqno.paralleljava.app.persistence.database;

import nl.jqno.paralleljava.TestData;
import nl.jqno.paralleljava.TestData.AnotherTodo;
import nl.jqno.paralleljava.TestData.SomeTodo;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.environment.Environment;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
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
    private static final TodoMapper todoMapper = new TodoMapper(TestData.URL_PREFIX);

    public void initialization() {

        test("a table is created", () -> {
            var jdbi = new DefaultJdbi(IN_MEMORY_DATABASE, todoMapper, NOP_LOGGER);
            var repo = new DatabaseRepository(jdbi);
            var result = repo.initialize();
            assertThat(result).isSuccess();
        });

        test("initializing twice is a no-op the second time", () -> {
            var jdbi = new DefaultJdbi(IN_MEMORY_DATABASE, todoMapper, NOP_LOGGER);
            var repo = new DatabaseRepository(jdbi);
            assertThat(repo.initialize()).isSuccess();
            assertThat(repo.initialize()).isSuccess();
        });

        test("a failure with no message while creating is propagated", () -> {
            var jdbi = new FailingJdbi();
            var repo = new DatabaseRepository(jdbi);
            assertThat(repo.initialize()).isFailure();
        });

        test("a failure with a message while creating is propagated", () -> {
            var jdbi = new FailingJdbi(new IllegalStateException("Something went wrong"));
            var repo = new DatabaseRepository(jdbi);
            assertThat(repo.initialize()).isFailure();
        });

        test("a failure to create the table is not propagated if the table already exists", () -> {
            var jdbi = new FailingJdbi(new IllegalStateException("Table \"TODO\" already exists"));
            var repo = new DatabaseRepository(jdbi);
            assertThat(repo.initialize()).isSuccess();
        });
    }

    public void repository() {
        var jdbi = new DefaultJdbi(IN_MEMORY_DATABASE, todoMapper, NOP_LOGGER);
        var repo = new DatabaseRepository(jdbi);

        beforeAll(() -> {
            assertThat(repo.initialize()).isSuccess();
            assertThat(repo.deleteAll()).isSuccess();
        });

        test("create a todo", () -> {
            var result = repo.create(SomeTodo.TODO);

            assertThat(result).isSuccess();
            assertThat(repo.getAll()).hasValueSatisfying(l -> l.contains(SomeTodo.TODO));
        });

        test("get a specific todo", () -> {
            repo.create(SomeTodo.TODO);

            assertThat(repo.get(SomeTodo.ID)).hasValueSatisfying(o -> assertThat(o).contains(SomeTodo.TODO));
            assertThat(repo.get(UUID.randomUUID())).hasValueSatisfying(o -> assertThat(o).isEmpty());
        });

        test("update a specific todo", () -> {
            var expected = SomeTodo.TODO.withTitle("another title");
            repo.create(SomeTodo.TODO);
            repo.create(AnotherTodo.TODO);

            var result = repo.update(expected);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).contains(expected);
        });

        test("delete a specific todo", () -> {
            repo.create(SomeTodo.TODO);
            repo.create(AnotherTodo.TODO);

            var result = repo.delete(SomeTodo.ID);
            var actual = repo.get(SomeTodo.ID).get();

            assertThat(result).isSuccess();
            assertThat(actual).isEmpty();
        });

        test("delete all todos", () -> {
            repo.create(SomeTodo.TODO);

            var result = repo.deleteAll();

            assertThat(result).isSuccess();
            assertThat(repo.getAll()).hasValueSatisfying(l -> assertThat(l).isEmpty());
        });
    }

    public void failures() {
        test("Execute failures cause failed results", () -> {
            var jdbi = new FailingJdbi();
            var repo = new DatabaseRepository(jdbi);

            var result = repo.create(SomeTodo.TODO);
            assertThat(result).isFailure();
        });

        test("Query failures cause failed results", () -> {
            var jdbi = new FailingJdbi();
            var repo = new DatabaseRepository(jdbi);

            var result = repo.getAll();
            assertThat(result).isFailure();
        });

        test("Mapping failures cause failed results", () -> {
            var failingMapper = new TodoMapper("") {
                public Todo map(ResultSet rs, StatementContext ctx) throws SQLException {
                    throw new SQLException("Intentional failure");
                }
            };
            var jdbi = new DefaultJdbi(IN_MEMORY_DATABASE, failingMapper, NOP_LOGGER);
            var repo = new DatabaseRepository(jdbi);
            repo.create(SomeTodo.TODO);

            var result = repo.getAll();
            assertThat(result).isFailure();
        });
    }
}
