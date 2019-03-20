package nl.jqno.paralleljava.app.endpoints;

import io.vavr.collection.List;
import nl.jqno.paralleljava.app.serialization.Serializer;

public class DefaultEndpoints implements Endpoints {
    private final Serializer serializer;

    public DefaultEndpoints(Serializer serializer) {
        this.serializer = serializer;
    }

    public Route get() {
        return ignored -> serializer.serializeTodos(List.empty());
    }

    public Route post() {
        return Request::body;
    }

    public Route delete() {
        return ignored -> "";
    }
}
