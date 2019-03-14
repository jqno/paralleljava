package nl.jqno.paralleljava.server;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import junit.framework.TestCase;
import nl.jqno.paralleljava.app.server.Heroku;

public class HerokuTest extends TestCase {

    private Heroku heroku;

    public void setUp() {
        heroku = new Heroku(HashMap.empty());
    }

    public void testValidPortInHeroku() {
        setEnvironmentVariable("PORT", "42");
        var actual = heroku.getAssignedPort();
        assertEquals(Option.of(42), actual);
    }

    public void testInvalidPortInHeroku() {
        setEnvironmentVariable("PORT", "this is not the port you're looking for");
        var actual = heroku.getAssignedPort();
        assertEquals(Option.none(), actual);
    }

    public void testGetPortOutsideOfHeroku() {
        var actual = heroku.getAssignedPort();
        assertEquals(Option.none(), actual);
    }

    private void setEnvironmentVariable(String key, String value) {
        var env = HashMap.of(key, value);
        heroku = new Heroku(env);
    }
}
