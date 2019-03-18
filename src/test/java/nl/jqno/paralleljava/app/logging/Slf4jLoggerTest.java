package nl.jqno.paralleljava.app.logging;

import nl.jqno.picotest.Test;
import org.slf4j.helpers.SubstituteLogger;

import static org.assertj.core.api.Assertions.assertThat;

public class Slf4jLoggerTest extends Test {

    private static final String SOME_MESSAGE = "";
    private static final Throwable SOME_EXCEPTION = new Throwable();

    private StubLogger underlying;
    private Slf4jLogger logger;

    public void logger() {
        beforeEach(() -> {
            underlying = new StubLogger();
            logger = new Slf4jLogger(underlying);
        });

        test("forDevelopment calls DEBUG", () -> {
            logger.forDevelopment(SOME_MESSAGE);
            assertThat(underlying.calledDebug).isEqualTo(1);
            assertThat(underlying.calledTotal()).isEqualTo(1);
            assertThat(underlying.lastThrowable).isNull();
        });

        test("forProduction calls INFO", () -> {
            logger.forProduction(SOME_MESSAGE);
            assertThat(underlying.calledInfo).isEqualTo(1);
            assertThat(underlying.calledTotal()).isEqualTo(1);
            assertThat(underlying.lastThrowable).isNull();
        });

        test("firstThingNextMorning calls WARN", () -> {
            logger.firstThingNextMorning(SOME_MESSAGE);
            assertThat(underlying.calledWarn).isEqualTo(1);
            assertThat(underlying.calledTotal()).isEqualTo(1);
            assertThat(underlying.lastThrowable).isNull();
        });

        test("firstThingNextMorning with exception calls WARN with exception", () -> {
            logger.firstThingNextMorning(SOME_MESSAGE, SOME_EXCEPTION);
            assertThat(underlying.calledWarn).isEqualTo(1);
            assertThat(underlying.calledTotal()).isEqualTo(1);
            assertThat(underlying.lastThrowable).isEqualTo(SOME_EXCEPTION);
        });

        test("wakeMeUp calls ERROR", () -> {
            logger.wakeMeUp(SOME_MESSAGE);
            assertThat(underlying.calledError).isEqualTo(1);
            assertThat(underlying.calledTotal()).isEqualTo(1);
            assertThat(underlying.lastThrowable).isNull();
        });

        test("wakeMeUp with exception calls ERROR with exception", () -> {
            logger.wakeMeUp(SOME_MESSAGE, SOME_EXCEPTION);
            assertThat(underlying.calledError).isEqualTo(1);
            assertThat(underlying.calledTotal()).isEqualTo(1);
            assertThat(underlying.lastThrowable).isEqualTo(SOME_EXCEPTION);
        });
    }

    private static class StubLogger extends SubstituteLogger {
        public int calledDebug = 0;
        public int calledInfo = 0;
        public int calledWarn = 0;
        public int calledError = 0;
        public Throwable lastThrowable = null;

        public StubLogger() {
            super(StubLogger.class.getName());
        }

        public int calledTotal() {
            return calledDebug + calledInfo + calledWarn + calledError;
        }

        public void debug(String msg) {
            calledDebug += 1;
        }

        public void info(String msg) {
            calledInfo += 1;
        }

        public void warn(String msg) {
            calledWarn += 1;
        }

        public void warn(String msg, Throwable e) {
            calledWarn += 1;
            lastThrowable = e;
        }

        public void error(String msg) {
            calledError += 1;
        }

        public void error(String msg, Throwable e) {
            calledError += 1;
            lastThrowable = e;
        }
    }
}

