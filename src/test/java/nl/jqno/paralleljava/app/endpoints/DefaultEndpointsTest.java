package nl.jqno.paralleljava.app.endpoints;

import io.vavr.collection.List;
import nl.jqno.paralleljava.app.persistence.InMemoryRepository;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEndpointsTest extends Test {

    public void endoints() {
        var serializer = WiredApplication.defaultSerializer();
        var repository = new InMemoryRepository();
        var someRequest = new Request("");
        var endpoints = new DefaultEndpoints(repository, serializer);

        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("get returns an empty list when no todos are present", () -> {
            var sut = endpoints.get();
            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo(serializer.serializeTodos(List.empty()));
        });

        test("get returns all todos", () -> {
            repository.createTodo(SOME_TODO);
            repository.createTodo(ANOTHER_TODO);
            var sut = endpoints.get();

            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo(SOME_SERIALIZED_LIST_OF_TODOS);
        });

        test("post adds a todo", () -> {
            var sut = endpoints.post();

            var actual = sut.handle(new Request(SOME_SERIALIZED_TODO));
            assertThat(actual).isEqualTo(SOME_SERIALIZED_TODO);
            assertThat(repository.getAllTodos()).contains(SOME_TODO);
        });

        test("delete clears all todos", () -> {
            repository.createTodo(SOME_TODO);
            var sut = endpoints.delete();

            var actual = sut.handle(someRequest);

            assertThat(actual).isEqualTo("");
            assertThat(repository.getAllTodos()).isEmpty();
        });
    }
}
