package nl.jqno.paralleljava.app.logging;

public interface Logger {
    void forDevelopment(String message);
    void forProduction(String message);
    void firstThingNextMorning(String message);
    void firstThingNextMorning(String message, Throwable t);
    void wakeMeUp(String message);
    void wakeMeUp(String message, Throwable t);
}
