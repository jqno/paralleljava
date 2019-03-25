package nl.jqno.paralleljava.app.environment;

import io.vavr.control.Option;

public interface Environment {
    Option<Integer> port();
    Option<String> hostUrl();
}
