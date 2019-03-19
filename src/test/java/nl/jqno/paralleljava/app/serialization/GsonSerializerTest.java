package nl.jqno.paralleljava.app.serialization;

import com.google.gson.Gson;
import io.vavr.control.Option;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.SOME_SERIALIZED_TODO;
import static nl.jqno.paralleljava.app.TestData.SOME_TODO;
import static org.assertj.core.api.Assertions.assertThat;

public class GsonSerializerTest extends Test {

    public void serialization() {

        var serializer = new GsonSerializer(new Gson());

        test("Serializes a Todo to json", () -> {
            var actual = serializer.serializeTodo(SOME_TODO);
            assertThat(actual)
                    .contains("\"id\":42")
                    .contains("\"title\":\"title\"")
                    .contains("\"url\":\"http://www.example.com\"")
                    .contains("\"completed\":true")
                    .contains("\"order\":1337");
        });

        test("Deserializes a Todo from json", () -> {
            var actual = serializer.deserializeTodo(SOME_SERIALIZED_TODO);
            assertThat(actual).isEqualTo(Option.of(SOME_TODO));
        });

        test("Deserialization returns none when json is invalid", () -> {
            var invalidJson = "this is an invalid json document";
            var actual = serializer.deserializeTodo(invalidJson);
            assertThat(actual).isEqualTo(Option.none());
        });

        test("Does a complete round-trip", () -> {
            var json = serializer.serializeTodo(SOME_TODO);
            var actual = serializer.deserializeTodo(json);
            assertThat(actual).isEqualTo(Option.of(SOME_TODO));
        });
    }
}
