package nl.jqno.paralleljava.app.endpoints;

import io.vavr.collection.List;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.paralleljava.app.persistence.InMemoryRepository;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEndpointsTest extends Test {

    public void endoints() {
        var logger = new NopLogger();
        var serializer = WiredApplication.defaultSerializer(logger);
        var repository = new InMemoryRepository(logger);
        var someRequest = new Request("");
        var urlBase = "/blabla/todo";
        var endpoints = new DefaultEndpoints(urlBase, repository, serializer, logger);

        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("get returns an empty list when no todos are present", () -> {
            var sut = endpoints.get();
            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo(serializer.serializeTodos(List.empty()));
        });

        test("get returns all todos", () -> {
            repository.createTodo(SomeTodo.TODO);
            repository.createTodo(AnotherTodo.TODO);
            var sut = endpoints.get();

            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo(ListOfTodos.SERIALIZED);
        });

        test("post adds a todo", () -> {
            var expected = new Todo(1, "title", urlBase + "/1", false, 0);
            var expectedSerialized = serializer.serializeTodo(expected);
            var sut = endpoints.post();

            var actual = sut.handle(new Request(SomeTodo.SERIALIZED_PARTIAL_POST));
            assertThat(actual).isEqualTo(expectedSerialized);
            assertThat(repository.getAllTodos()).contains(expected);
        });

        test("delete clears all todos", () -> {
            repository.createTodo(SomeTodo.TODO);
            var sut = endpoints.delete();

            var actual = sut.handle(someRequest);

            assertThat(actual).isEqualTo("");
            assertThat(repository.getAllTodos()).isEmpty();
        });
    }
}
