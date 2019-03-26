package nl.jqno.paralleljava.app.persistence.database;

import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.dependencyinjection.Wiring;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseRepositoryTest extends Test {

    private final Repository repo = Wiring.databaseRepository("jdbc:h2:mem:test");

    public void initialization() {
        test("a table is created", () -> {
            var result = repo.initialize();
            assertThat(result.isSuccess()).isTrue();
        });
    }

    public void crud() {
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
}
