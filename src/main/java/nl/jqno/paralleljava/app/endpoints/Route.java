package nl.jqno.paralleljava.app.endpoints;

public interface Route {
    String handle(Request request);
}
