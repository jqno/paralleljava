package nl.jqno.paralleljava.app.logging;

public class Slf4jLogger implements Logger {
    private final org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public void forDevelopment(String message) {
        logger.debug(message);
    }

    public void forProduction(String message) {
        logger.info(message);
    }

    public void firstThingNextMorning(String message) {
        logger.warn(message);
    }

    public void firstThingNextMorning(String message, Throwable t) {
        logger.warn(message, t);
    }

    public void wakeMeUp(String message) {
        logger.error(message);
    }

    public void wakeMeUp(String message, Throwable t) {
        logger.error(message, t);
    }
}
