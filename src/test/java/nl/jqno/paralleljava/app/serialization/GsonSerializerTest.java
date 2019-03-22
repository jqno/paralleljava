package nl.jqno.paralleljava.app.serialization;

import io.vavr.collection.List;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GsonSerializerTest extends Test {

    private Serializer serializer = WiredApplication.defaultSerializer(new NopLogger());

    public void serializationOfASingleTodo() {

        test("Serializes a Todo to json", () -> {
            var actual = serializer.serializeTodo(SomeTodo.TODO);
            assertThat(actual)
                    .contains("\"id\":\"" + SomeTodo.ID + "\"")
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

    public void serializationOfACompletePartialTodo() {
        test("Serializes a complete PartialTodo to json", () -> {
            var actual = serializer.serializePartialTodo(SomeTodo.PARTIAL_COMPLETE);
            assertThat(actual)
                    .contains("\"id\":\"" + SomeTodo.ID + "\"")
                    .contains("\"title\":\"title\"")
                    .contains("\"url\":\"http://www.example.com\"")
                    .contains("\"completed\":true")
                    .contains("\"order\":1337");
        });

        test("Deserializes a complete PartialTodo from json", () -> {
            var actual = serializer.deserializePartialTodo(SomeTodo.SERIALIZED);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.PARTIAL_COMPLETE));
        });

        test("Deserialization of a complete PartialTodo returns none when json is invalid", () -> {
            var invalidJson = "this is an invalid json document";
            var actual = serializer.deserializePartialTodo(invalidJson);
            assertThat(actual).isEqualTo(Option.none());
        });

        test("Does a complete round-trip on PartialTodo", () -> {
            var json = serializer.serializePartialTodo(SomeTodo.PARTIAL_COMPLETE);
            var actual = serializer.deserializePartialTodo(json);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.PARTIAL_COMPLETE));
        });
    }

    public void serializationOfAPOSTedPartialTodo() {
        test("Serializes a POSTed PartialTodo to json", () -> {
            var actual = serializer.serializePartialTodo(SomeTodo.PARTIAL_POST);
            assertThat(actual)
                    .contains("\"title\":\"title\"")
                    .doesNotContain("\"id\":")
                    .doesNotContain("\"url\":")
                    .doesNotContain("\"completed\":")
                    .doesNotContain("\"order\":");
        });

        test("Deserializes a POSTed PartialTodo from json", () -> {
            var actual = serializer.deserializePartialTodo(SomeTodo.SERIALIZED_PARTIAL_POST);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.PARTIAL_POST));
        });

        test("Does a complete round-trip on a POSTed PartialTodo", () -> {
            var json = serializer.serializePartialTodo(SomeTodo.PARTIAL_POST);
            var actual = serializer.deserializePartialTodo(json);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.PARTIAL_POST));
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

    public void serializationOfAUuid() {

        test("Serializes a UUID to json", () -> {
            var actual = serializer.serializeUuid(SomeTodo.ID);
            assertThat(actual).isEqualTo("\"" + SomeTodo.ID.toString() + "\"");
        });

        test("Deserializes a UUID from json", () -> {
            var actual = serializer.deserializeUuid(SomeTodo.ID.toString());
            assertThat(actual).isEqualTo(Option.of(SomeTodo.ID));
        });

        test("Deserialization of a UUID returns none when json is invalid", () -> {
            var invalidJson = "this is an invalid json document";
            var actual = serializer.deserializeUuid(invalidJson);
            assertThat(actual).isEqualTo(Option.none());
        });

        test("Does a complete round-trip on UUID", () -> {
            var json = serializer.serializeUuid(SomeTodo.ID);
            var actual = serializer.deserializeUuid(json);
            assertThat(actual).isEqualTo(Option.of(SomeTodo.ID));
        });
    }
}
