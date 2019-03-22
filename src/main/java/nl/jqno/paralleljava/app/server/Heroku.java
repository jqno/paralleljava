package nl.jqno.paralleljava.app.server;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import io.vavr.control.Try;

public class Heroku {

    private final Map<String, String> env;

    public Heroku(Map<String, String> env) {
        this.env = env;
    }

    public Option<Integer> getAssignedPort() {
        return env.get("PORT").flatMap(this::parse);
    }

    public Option<String> getHostUrl() {
        // hard-coded for now
        return Option.some("https://parallel-java.herokuapp.com");
    }

    private Option<Integer> parse(String port) {
        return Try.of(() -> Integer.parseInt(port)).toOption();
    }
}
