package nl.jqno.paralleljava.app.controller;

public interface Controller {
    String get();
    String post(String json);
    String delete();
}
