package nl.jqno.paralleljava.app.controller;

import io.vavr.collection.List;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.paralleljava.app.persistence.ConstantIdGenerator;
import nl.jqno.paralleljava.app.persistence.InMemoryRepository;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static nl.jqno.paralleljava.app.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultControllerTest extends Test {

    public void endoints() {
        var logger = new NopLogger();
        var repository = new InMemoryRepository(logger);
        var constantId = UUID.randomUUID();
        var idGenerator = new ConstantIdGenerator(constantId);
        var serializer = WiredApplication.defaultSerializer(logger);
        var urlBase = "/blabla/todo";
        var controller = new DefaultController(urlBase, repository, idGenerator, serializer, logger);

        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("get returns an empty list when no todos are present", () -> {
            var actual = controller.get();
            assertThat(actual).isEqualTo(serializer.serializeTodos(List.empty()));
        });

        test("get returns all todos", () -> {
            repository.createTodo(SomeTodo.TODO);
            repository.createTodo(AnotherTodo.TODO);

            var actual = controller.get();
            assertThat(actual).isEqualTo(ListOfTodos.SERIALIZED);
        });

        test("post adds a todo", () -> {
            var expected = new Todo(constantId, "title", urlBase + "/" + constantId, false, 0);
            var expectedSerialized = serializer.serializeTodo(expected);

            var actual = controller.post(SomeTodo.SERIALIZED_PARTIAL_POST);
            assertThat(actual).isEqualTo(expectedSerialized);
            assertThat(repository.getAllTodos()).contains(expected);
        });

        test("delete clears all todos", () -> {
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.delete();

            assertThat(actual).isEqualTo("");
            assertThat(repository.getAllTodos()).isEmpty();
        });
    }
}
