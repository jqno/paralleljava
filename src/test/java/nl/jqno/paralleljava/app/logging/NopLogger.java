package nl.jqno.paralleljava.app.logging;

import nl.jqno.paralleljava.app.logging.Logger;

public class NopLogger implements Logger {

    public void forDevelopment(String message) {}
    public void forProduction(String message) {}
    public void firstThingNextMorning(String message) {}
    public void firstThingNextMorning(String message, Throwable t) {}
    public void wakeMeUp(String message) {}
    public void wakeMeUp(String message, Throwable t) {}
}
