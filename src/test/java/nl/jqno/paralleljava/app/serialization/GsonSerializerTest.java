package nl.jqno.paralleljava.app.serialization;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GsonSerializerTest extends Test {

    private Serializer serializer = WiredApplication.defaultSerializer();

    public void serializationOfASingleTodo() {

        test("Serializes a Todo to json", () -> {
            var actual = serializer.serializeTodo(SomeTodo.TODO);
            assertThat(actual)
                    .contains("\"id\":42")
                    .contains("\"title\":\"title\"")
                    .contains("\"url\":\"http://www.example.com\"")
                    .contains("\"completed\":true")
                    .contains("\"order\":1337");
        });

        test("Deserializes a Todo from json", () -> {
            var actual = serializer.deserializeTodo(SomeTodo.SERIALIZED);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.TODO));
        });

        test("Deserialization of a Todo returns none when json is invalid", () -> {
            var invalidJson = "this is an invalid json document";
            var actual = serializer.deserializeTodo(invalidJson);
            assertThat(actual).isEqualTo(Option.none());
        });

        test("Does a complete round-trip on Todo", () -> {
            var json = serializer.serializeTodo(SomeTodo.TODO);
            var actual = serializer.deserializeTodo(json);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.TODO));
        });
    }

    public void serializationOfAListOfTodos() {

        test("Serializes a list of Todos to json", () -> {
            var actual = serializer.serializeTodos(ListOfTodos.LIST);
            assertThat(actual)
                    .contains(SomeTodo.SERIALIZED)
                    .contains(AnotherTodo.SERIALIZED);
        });

        test("Deserializes a list of Todos from json", () -> {
            var actual = serializer.deserializeTodos(ListOfTodos.SERIALIZED);
            assertThat(actual).containsExactlyElementsOf(ListOfTodos.LIST);
        });

        test("Deserialization of a list of Todos returns an empty list when json is invalid", () -> {
            var invalidJson = SomeTodo.SERIALIZED; // but not a list
            var actual = serializer.deserializeTodos(invalidJson);
            assertThat(actual).isEqualTo(List.empty());
        });

        test("Does a complete round-trip on lists of Todos", () -> {
            var json = serializer.serializeTodos(ListOfTodos.LIST);
            var actual = serializer.deserializeTodos(json);
            assertThat(actual).isEqualTo(ListOfTodos.LIST);
        });
    }
}
