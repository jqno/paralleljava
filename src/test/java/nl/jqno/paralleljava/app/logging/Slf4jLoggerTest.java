package nl.jqno.paralleljava.app.logging;

import nl.jqno.paralleljava.dependencyinjection.TestWiring;
import nl.jqno.paralleljava.dependencyinjection.stubs.StubLogger;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Slf4jLoggerTest extends Test {

    private static final String SOME_MESSAGE = "";
    private static final Throwable SOME_EXCEPTION = new Throwable();

    private StubLogger underlying;
    private Slf4jLogger logger;

    public void logger() {
        beforeEach(() -> {
            underlying = TestWiring.stubLogger();
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
}

