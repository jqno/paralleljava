package nl.jqno.paralleljava.app.endpoints;

import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestTest extends Test {

    public void request() {
        var sut = new Request("body");

        test("getters", () -> {
            assertThat(sut.body()).isEqualTo("body");
        });
    }
}
