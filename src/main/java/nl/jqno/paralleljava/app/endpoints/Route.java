package nl.jqno.paralleljava.app.endpoints;

import io.vavr.collection.Map;

public interface Route {
    String handle(Map<String, String> request);
}
