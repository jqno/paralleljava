package nl.jqno.paralleljava.app.environment;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HerokuEnvironmentTest extends Test {

    private Environment environment = TestWiring.mapBasedHerokuEnvironment(HashMap.empty());

    public void port() {
        test("a valid port is provided", () -> {
            setEnvironmentVariable("PORT", "42");
            var actual = environment.port();
            assertThat(actual).isEqualTo(Option.of(42));
        });

        test("an invalid port is provided", () -> {
            setEnvironmentVariable("PORT", "this is not the port you're looking for");
            var actual = environment.port();
            assertThat(actual).isEqualTo(Option.none());
        });

        test("no port is provided", () -> {
            var actual = environment.port();
            assertThat(actual).isEqualTo(Option.none());
        });

        test("host url", () -> {
            var actual = environment.hostUrl();
            assertThat(actual).isEqualTo(Option.some("https://parallel-java.herokuapp.com"));
        });
    }

    private void setEnvironmentVariable(String key, String value) {
        var env = HashMap.of(key, value);
        environment = TestWiring.mapBasedHerokuEnvironment(env);
    }
}
