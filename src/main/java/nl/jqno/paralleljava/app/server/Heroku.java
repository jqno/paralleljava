package nl.jqno.paralleljava.app.server;

import io.vavr.control.Option;
import io.vavr.control.Try;

public class Heroku {

    private ProcessBuilder processBuilder;

    public Heroku(ProcessBuilder processBuilder) {
        this.processBuilder = processBuilder;
    }

    public Option<Integer> getAssignedPort() {
        var port = Option.of(processBuilder.environment().get("PORT"));
        return port.flatMap(this::parse);
    }

    private Option<Integer> parse(String port) {
        return Try.of(() -> Integer.parseInt(port)).toOption();
    }
}
