package nl.jqno.paralleljava.app.logging;

public class NopLogger implements Logger {

    public static final Logger INSTANCE = new NopLogger();

    public void forDevelopment(String message) {}
    public void forProduction(String message) {}
    public void firstThingNextMorning(String message) {}
    public void firstThingNextMorning(String message, Throwable t) {}
    public void wakeMeUp(String message) {}
    public void wakeMeUp(String message, Throwable t) {}
}
