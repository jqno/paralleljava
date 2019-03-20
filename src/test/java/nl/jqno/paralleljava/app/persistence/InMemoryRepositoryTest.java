package nl.jqno.paralleljava.app.persistence;

import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.SOME_TODO;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRepositoryTest extends Test {

    public void repository() {
        var repo = new InMemoryRepository();

        beforeEach(() -> {
            repo.clearAllTodos();
        });

        test("create a todo", () -> {
            repo.createTodo(SOME_TODO);
            assertThat(repo.getAllTodos()).contains(SOME_TODO);
        });

        test("clearing all todos", () -> {
            repo.createTodo(SOME_TODO);
            repo.clearAllTodos();
            assertThat(repo.getAllTodos()).isEmpty();
        });
    }
}
