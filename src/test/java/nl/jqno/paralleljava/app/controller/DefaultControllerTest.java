package nl.jqno.paralleljava.app.controller;

import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.persistence.IdGenerator;
import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.serialization.Serializer;
import nl.jqno.paralleljava.dependencyinjection.Wiring;
import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.picotest.Test;

import java.util.UUID;

import static nl.jqno.paralleljava.dependencyinjection.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultControllerTest extends Test {

    private final UUID constantId = UUID.randomUUID();
    private final String fullUrl = "/blabla/todo";

    private final LoggerFactory loggerFactory = TestWiring.nopLoggerFactory();
    private final Repository repository = Wiring.inMemoryRepository(loggerFactory);
    private final IdGenerator idGenerator = TestWiring.constantIdGenerator(constantId);
    private final Serializer serializer = Wiring.gsonSerializer(loggerFactory);

    private final DefaultController controller = new DefaultController(fullUrl, repository, idGenerator, serializer, loggerFactory);

    public void get() {
        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("get returns an empty list when no todos are present", () -> {
            var actual = controller.get();
            assertThat(actual).isEqualTo(Try.success("[]"));
        });

        test("get returns all todos", () -> {
            repository.createTodo(SomeTodo.TODO);
            repository.createTodo(AnotherTodo.TODO);

            var actual = controller.get();
            assertThat(actual).isEqualTo(Try.success(ListOfTodos.SERIALIZED));
        });
    }

    public void getWithId() {
        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("get with id returns a specific serialized todo if it exists", () -> {
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.get(SomeTodo.ID.toString());
            assertThat(actual).isEqualTo(Try.success(SomeTodo.SERIALIZED));
        });

        test("get with id fails if id is invalid", () -> {
            var actual = controller.get(Invalid.ID);
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });

        test("get with id fails if it doesn't exist", () -> {
            var actual = controller.get(SomeTodo.ID.toString());
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });
    }

    public void post() {
        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("post adds a todo without order", () -> {
            var expected = new Todo(constantId, "title", fullUrl + "/" + constantId, false, 0);
            var expectedSerialized = serializer.serializeTodo(expected);

            var actual = controller.post(SomeTodo.SERIALIZED_PARTIAL_POST);
            assertThat(actual).isEqualTo(Try.success(expectedSerialized));
            assertThat(repository.getAllTodos().get()).contains(expected);
        });

        test("post adds a todo with order", () -> {
            var expected = new Todo(constantId, "title", fullUrl + "/" + constantId, false, 1337);
            var expectedSerialized = serializer.serializeTodo(expected);

            var actual = controller.post(SomeTodo.SERIALIZED_PARTIAL_POST_WITH_ORDER);
            assertThat(actual).isEqualTo(Try.success(expectedSerialized));
            assertThat(repository.getAllTodos().get()).contains(expected);
        });

        test("post fails when todo is invalid", () -> {
            var actual = controller.post(Invalid.JSON);
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });

        test("post fails when todo has no title", () -> {
            var actual = controller.post(Invalid.SERIALIZED_TODO_WITH_NO_TITLE);
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });
    }

    public void patch() {
        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("patch changes title", () -> {
            repository.createTodo(SomeTodo.TODO);
            var expected = SomeTodo.TODO.withTitle("another title");

            var result = controller.patch(SomeTodo.ID.toString(), "{\"title\":\"another title\"}");
            var actual = repository.get(SomeTodo.ID);

            assertThat(result).isEqualTo(Try.success(serializer.serializeTodo(expected)));
            assertThat(actual.get()).isEqualTo(Option.some(expected));
        });

        test("patch changes completed", () -> {
            repository.createTodo(SomeTodo.TODO);
            var expected = SomeTodo.TODO.withCompleted(false);

            var result = controller.patch(SomeTodo.ID.toString(), "{\"completed\":false}");
            var actual = repository.get(SomeTodo.ID);

            assertThat(result).isEqualTo(Try.success(serializer.serializeTodo(expected)));
            assertThat(actual.get()).isEqualTo(Option.some(expected));
        });

        test("patch changes order", () -> {
            repository.createTodo(SomeTodo.TODO);
            var expected = SomeTodo.TODO.withOrder(47);

            var result = controller.patch(SomeTodo.ID.toString(), "{\"order\":47}");
            var actual = repository.get(SomeTodo.ID);

            assertThat(result).isEqualTo(Try.success(serializer.serializeTodo(expected)));
            assertThat(actual.get()).isEqualTo(Option.some(expected));
        });

        test("delete with id fails if id is invalid", () -> {
            var actual = controller.patch(Invalid.ID, "{\"order\":47}");
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });

        test("post fails when todo is invalid", () -> {
            var actual = controller.patch(SomeTodo.ID.toString(), Invalid.JSON);
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });

        test("patch fails if id doesn't exist", () -> {
            var actual = controller.patch(SomeTodo.ID.toString(), "{\"order\":47}");
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });
    }

    public void delete() {
        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("delete clears all todos", () -> {
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.delete();

            assertThat(actual).isEqualTo(Try.success(""));
            assertThat(repository.getAllTodos().get()).isEmpty();
        });
    }

    public void deleteWithId() {
        beforeEach(() -> {
            repository.clearAllTodos();
        });

        test("delete with id removes the corresponding todo", () -> {
            repository.createTodo(AnotherTodo.TODO);
            repository.createTodo(SomeTodo.TODO);

            var actual = controller.delete(SomeTodo.ID.toString());

            assertThat(actual).isEqualTo(Try.success(""));
            assertThat(repository.getAllTodos().get())
                    .doesNotContain(SomeTodo.TODO)
                    .contains(AnotherTodo.TODO);
        });

        test("delete with id fails if id is invalid", () -> {
            var actual = controller.delete(Invalid.ID);
            assertThat(actual.isFailure()).isTrue();
            assertThat(actual.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        });
    }
}
