package nl.jqno.paralleljava.app.endpoints;

import io.vavr.collection.List;
import nl.jqno.paralleljava.dependencyinjection.WiredApplication;
import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.SOME_SERIALIZED_TODO;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultEndpointsTest extends Test {

    public void endoints() {
        var serializer = WiredApplication.defaultSerializer();
        var someRequest = new Request("body");
        var endpoints = new DefaultEndpoints(serializer);

        test("get returns an empty array", () -> {
            var sut = endpoints.get();
            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo(serializer.serializeTodos(List.empty()));
        });

        test("post responds with the todo that was posted to it", () -> {
            var sut = endpoints.post();
            var actual = sut.handle(new Request(SOME_SERIALIZED_TODO));
            assertThat(actual).isEqualTo(SOME_SERIALIZED_TODO);
        });

        test("delete basically does nothing", () -> {
            var sut = endpoints.delete();
            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo("");
        });
    }
}
