package nl.jqno.paralleljava.app.controller;

import io.vavr.control.Try;

public interface Controller {
    Try<String> get();
    String get(String id);
    String post(String json);
    String patch(String id, String json);
    String delete();
    String delete(String id);
}
