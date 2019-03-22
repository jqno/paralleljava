package nl.jqno.paralleljava.app.controller;

import io.vavr.collection.List;
import io.vavr.control.Option;
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
        var loggerFactory = NopLogger.FACTORY;
        var repository = new InMemoryRepository(loggerFactory);
        var constantId = UUID.randomUUID();
        var idGenerator = new ConstantIdGenerator(constantId);
        var serializer = WiredApplication.defaultSerializer(loggerFactory);
        var urlBase = "/blabla/todo";
        var controller = new DefaultController(urlBase, repository, idGenerator, serializer, loggerFactory);

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

        test("get with id returns a specific serialized todo if it exists", () -> {
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.get(SomeTodo.ID.toString());
            assertThat(actual).isEqualTo(SomeTodo.SERIALIZED);
        });

        test("get with id returns nothing if it doesn't exist", () -> {
            var actual = controller.get(SomeTodo.ID.toString());
            assertThat(actual).isEqualTo("");
        });

        test("post adds a todo without order", () -> {
            var expected = new Todo(constantId, "title", urlBase + "/" + constantId, false, 0);
            var expectedSerialized = serializer.serializeTodo(expected);

            var actual = controller.post(SomeTodo.SERIALIZED_PARTIAL_POST);
            assertThat(actual).isEqualTo(expectedSerialized);
            assertThat(repository.getAllTodos()).contains(expected);
        });

        test("post adds a todo with order", () -> {
            var expected = new Todo(constantId, "title", urlBase + "/" + constantId, false, 1337);
            var expectedSerialized = serializer.serializeTodo(expected);

            var actual = controller.post(SomeTodo.SERIALIZED_PARTIAL_POST_WITH_ORDER);
            assertThat(actual).isEqualTo(expectedSerialized);
            assertThat(repository.getAllTodos()).contains(expected);
        });

        test("patch changes title", () -> {
            repository.createTodo(SomeTodo.TODO);
            var expected = SomeTodo.TODO.withTitle("another title");

            var result = controller.patch(SomeTodo.ID.toString(), "{\"title\":\"another title\"}");
            var actual = repository.get(SomeTodo.ID);

            assertThat(result).isEqualTo(serializer.serializeTodo(expected));
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("patch changes completed", () -> {
            repository.createTodo(SomeTodo.TODO);
            var expected = SomeTodo.TODO.withCompleted(false);

            var result = controller.patch(SomeTodo.ID.toString(), "{\"completed\":false}");
            var actual = repository.get(SomeTodo.ID);

            assertThat(result).isEqualTo(serializer.serializeTodo(expected));
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("patch changes order", () -> {
            repository.createTodo(SomeTodo.TODO);
            var expected = SomeTodo.TODO.withOrder(47);

            var result = controller.patch(SomeTodo.ID.toString(), "{\"order\":47}");
            var actual = repository.get(SomeTodo.ID);

            assertThat(result).isEqualTo(serializer.serializeTodo(expected));
            assertThat(actual).isEqualTo(Option.some(expected));
        });

        test("delete clears all todos", () -> {
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.delete();

            assertThat(actual).isEqualTo("");
            assertThat(repository.getAllTodos()).isEmpty();
        });

        test("delete with id removes the corresponding todo", () -> {
            repository.createTodo(AnotherTodo.TODO);
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.delete(SomeTodo.ID.toString());

            assertThat(actual).isEqualTo("");
            assertThat(repository.getAllTodos())
                    .doesNotContain(SomeTodo.TODO)
                    .contains(AnotherTodo.TODO);
        });
    }
}
