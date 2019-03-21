package nl.jqno.paralleljava.app.persistence;

import nl.jqno.paralleljava.app.TestData.SomeTodo;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRepositoryTest extends Test {

    public void repository() {
        var repo = new InMemoryRepository(new NopLogger());

        beforeEach(() -> {
            repo.clearAllTodos();
        });

        test("create a todo", () -> {
            repo.createTodo(SomeTodo.TODO);
            assertThat(repo.getAllTodos()).contains(SomeTodo.TODO);
        });

        test("clearing all todos", () -> {
            repo.createTodo(SomeTodo.TODO);
            repo.clearAllTodos();
            assertThat(repo.getAllTodos()).isEmpty();
        });
    }
}
