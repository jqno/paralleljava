package nl.jqno.paralleljava.app.controller;

public interface Controller {
    String get();
    String get(String json);
    String post(String json);
    String delete();
}
