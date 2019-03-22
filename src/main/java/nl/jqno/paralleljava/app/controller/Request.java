package nl.jqno.paralleljava.app.controller;

public class Request {
    private final String body;

    public Request(String body) {
        this.body = body;
    }

    public String body() {
        return body;
    }
}
