package nl.jqno.paralleljava.app.endpoints;

public class DefaultEndpoints implements Endpoints {

    public Route helloWorld() {
        return ignored -> "Hello world";
    }

    public Route post() {
        return Request::body;
    }

    public Route delete() {
        return ignored -> "";
    }
}
