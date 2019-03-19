package nl.jqno.paralleljava.app.endpoints;

import nl.jqno.picotest.Test;

import static nl.jqno.paralleljava.app.TestData.SOME_SERIALIZED_TODO;
import static org.assertj.core.api.Assertions.assertThat;

public class EndpointsTest extends Test {

    public void endoints() {
        var someRequest = new Request("body");
        var endpoints = new DefaultEndpoints();

        test("hello world works", () -> {
            var sut = endpoints.helloWorld();
            var actual = sut.handle(someRequest);
            assertThat(actual).isEqualTo("Hello world");
        });

        test("post responds with the todo that was posted to it", () -> {
            var sut = endpoints.post();
            var actual = sut.handle(new Request(SOME_SERIALIZED_TODO));
            assertThat(actual).isEqualTo(SOME_SERIALIZED_TODO);
        });
    }
}
