package nl.jqno.paralleljava.server;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import nl.jqno.paralleljava.app.server.Heroku;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HerokuTest extends Test {

    private Heroku heroku = new Heroku(HashMap.empty());

    public void port() {
        test("a valid port is provided", () -> {
            setEnvironmentVariable("PORT", "42");
            var actual = heroku.getAssignedPort();
            assertThat(actual).isEqualTo(Option.of(42));
        });

        test("an invalid port is provided", () -> {
            setEnvironmentVariable("PORT", "this is not the port you're looking for");
            var actual = heroku.getAssignedPort();
            assertThat(actual).isEqualTo(Option.none());
        });

        test("no port is provided", () -> {
            var actual = heroku.getAssignedPort();
            assertThat(actual).isEqualTo(Option.none());
        });
    }

    private void setEnvironmentVariable(String key, String value) {
        var env = HashMap.of(key, value);
        heroku = new Heroku(env);
    }
}
