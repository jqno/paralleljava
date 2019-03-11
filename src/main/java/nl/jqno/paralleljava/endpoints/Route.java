package nl.jqno.paralleljava.endpoints;

import io.vavr.collection.Map;

public interface Route {
    String handle(Map<String, String> request);
}
