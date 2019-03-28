package nl.jqno.paralleljava.app.environment;

import io.vavr.collection.HashMap;
import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.picotest.Test;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

public class HerokuEnvironmentTest extends Test {

    private Environment environment = TestWiring.mapBasedHerokuEnvironment(HashMap.empty());

    public void port() {
        test("a valid port is provided", () -> {
            setEnvironmentVariable("PORT", "42");
            var actual = environment.port();
            assertThat(actual).contains(42);
        });

        test("an invalid port is provided", () -> {
            setEnvironmentVariable("PORT", "this is not the port you're looking for");
            var actual = environment.port();
            assertThat(actual).isEmpty();
        });

        test("no port is provided", () -> {
            var actual = environment.port();
            assertThat(actual).isEmpty();
        });

        test("host url", () -> {
            var actual = environment.hostUrl();
            assertThat(actual).contains("https://parallel-java.herokuapp.com");
        });

        test("jdbc url", () -> {
            setEnvironmentVariable("JDBC_DATABASE_URL", "some-jdbc");
            var actual = environment.jdbcUrl();
            assertThat(actual).contains("some-jdbc");
        });
    }

    private void setEnvironmentVariable(String key, String value) {
        var env = HashMap.of(key, value);
        environment = TestWiring.mapBasedHerokuEnvironment(env);
    }
}
