package nl.jqno.paralleljava.app.environment;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class HerokuEnvironment implements Environment {

    private final Map<String, String> env;

    public HerokuEnvironment(Map<String, String> env) {
        this.env = env;
    }

    public Option<Integer> port() {
        return env.get("PORT").flatMap(this::parse);
    }

    public Option<String> hostUrl() {
        // hard-coded for now
        return Option.some("https://parallel-java.herokuapp.com");
    }

    private Option<Integer> parse(String port) {
        return Try.of(() -> Integer.parseInt(port)).toOption();
    }
}
