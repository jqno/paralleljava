package nl.jqno.paralleljava.app.serialization;

import nl.jqno.paralleljava.app.logging.NopLogger;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class GsonSerializerTest extends Test {

    private Serializer serializer = GsonSerializer.create(c -> new NopLogger());

    public void serializationOfASingleTodo() {

        test("Serializes a Todo to json", () -> {
            var actual = serializer.serializeTodo(SomeTodo.TODO);
            assertThat(actual)
                    .contains("\"id\":\"" + SomeTodo.ID + "\"")
                    .contains("\"title\":\"title\"")
                    .contains("\"url\":\"" + SomeTodo.URL + "\"")
                    .contains("\"completed\":true")
                    .contains("\"order\":1337");
        });

        test("Deserializes a Todo from json", () -> {
            var actual = serializer.deserializeTodo(SomeTodo.SERIALIZED);
            assertThat(actual).contains(SomeTodo.TODO);
        });

        test("Deserialization of a Todo returns none when json is invalid", () -> {
            var actual = serializer.deserializeTodo(Invalid.JSON);
            assertThat(actual).isEmpty();
        });

        test("Does a complete round-trip on Todo", () -> {
            var json = serializer.serializeTodo(SomeTodo.TODO);
            var actual = serializer.deserializeTodo(json);
            assertThat(actual).contains(SomeTodo.TODO);
        });
    }

    public void serializationOfACompletePartialTodo() {
        test("Serializes a complete PartialTodo to json", () -> {
            var actual = serializer.serializePartialTodo(SomeTodo.PARTIAL_COMPLETE);
            assertThat(actual)
                    .contains("\"id\":\"" + SomeTodo.ID + "\"")
                    .contains("\"title\":\"title\"")
                    .contains("\"url\":\"" + SomeTodo.URL + "\"")
                    .contains("\"completed\":true")
                    .contains("\"order\":1337");
        });

        test("Deserializes a complete PartialTodo from json", () -> {
            var actual = serializer.deserializePartialTodo(SomeTodo.SERIALIZED);
            assertThat(actual).contains(SomeTodo.PARTIAL_COMPLETE);
        });

        test("Deserialization of a complete PartialTodo returns none when json is invalid", () -> {
            var actual = serializer.deserializePartialTodo(Invalid.JSON);
            assertThat(actual).isEmpty();
        });

        test("Does a complete round-trip on PartialTodo", () -> {
            var json = serializer.serializePartialTodo(SomeTodo.PARTIAL_COMPLETE);
            var actual = serializer.deserializePartialTodo(json);
            assertThat(actual).contains(SomeTodo.PARTIAL_COMPLETE);
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
            assertThat(actual).contains(SomeTodo.PARTIAL_POST);
        });

        test("Does a complete round-trip on a POSTed PartialTodo", () -> {
            var json = serializer.serializePartialTodo(SomeTodo.PARTIAL_POST);
            var actual = serializer.deserializePartialTodo(json);
            assertThat(actual).contains(SomeTodo.PARTIAL_POST);
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
            assertThat(actual).isEmpty();
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
            assertThat(actual).contains(SomeTodo.ID);
        });

        test("Deserialization of a UUID returns none when json is invalid", () -> {
            var actual = serializer.deserializeUuid(Invalid.ID);
            assertThat(actual).isEmpty();
        });

        test("Does a complete round-trip on UUID", () -> {
            var json = serializer.serializeUuid(SomeTodo.ID);
            var actual = serializer.deserializeUuid(json);
            assertThat(actual).contains(SomeTodo.ID);
        });
    }
}
