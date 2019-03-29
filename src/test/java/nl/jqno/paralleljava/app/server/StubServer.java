package nl.jqno.paralleljava.app.server;

public class StubServer implements Server {
    public int calledRun = 0;

    public void clear() {
        calledRun = 0;
    }

    public void run() {
        calledRun += 1;
    }
}
