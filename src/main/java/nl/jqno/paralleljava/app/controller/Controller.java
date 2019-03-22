package nl.jqno.paralleljava.app.controller;

public interface Controller {
    String get();
    String get(String id);
    String post(String json);
    String patch(String id, String json);
    String delete();
    String delete(String id);
}
