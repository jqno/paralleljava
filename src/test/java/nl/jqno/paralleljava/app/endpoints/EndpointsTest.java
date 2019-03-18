package nl.jqno.paralleljava.app.endpoints;

import io.vavr.collection.HashMap;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EndpointsTest extends Test {

    public void endoints() {
        var endpoints = new Endpoints();

        test("hello world works", () -> {
            var sut = endpoints.helloWorld();
            assertThat(sut.handle(HashMap.empty())).isEqualTo("Hello world");
        });
    }
}
