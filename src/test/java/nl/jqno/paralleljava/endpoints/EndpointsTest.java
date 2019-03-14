package nl.jqno.paralleljava.endpoints;

import io.vavr.collection.HashMap;
import junit.framework.TestCase;
import nl.jqno.paralleljava.app.endpoints.Endpoints;

public class EndpointsTest extends TestCase {

    private final Endpoints endpoints = new Endpoints();

    public void testHelloWorld() {
        var sut = endpoints.helloWorld();
        assertEquals("Hello world", sut.handle(HashMap.empty()));
    }
}
