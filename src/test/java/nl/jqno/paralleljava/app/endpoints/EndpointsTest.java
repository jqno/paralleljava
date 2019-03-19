package nl.jqno.paralleljava.app.endpoints;

import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EndpointsTest extends Test {

    public void endoints() {
        var someRequest = new Request("body");
        var endpoints = new DefaultEndpoints();

        test("hello world works", () -> {
            var sut = endpoints.helloWorld();
            assertThat(sut.handle(someRequest)).isEqualTo("Hello world");
        });
    }
}
