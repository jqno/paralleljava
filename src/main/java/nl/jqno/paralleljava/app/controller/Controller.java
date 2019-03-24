package nl.jqno.paralleljava.app.controller;

import io.vavr.control.Try;

public interface Controller {
    Try<String> get();
    Try<String> get(String id);
    Try<String> post(String json);
    Try<String> patch(String id, String json);
    Try<String> delete();
    Try<String> delete(String id);
}
