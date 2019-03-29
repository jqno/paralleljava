package nl.jqno.paralleljava.app.logging;

import org.slf4j.helpers.SubstituteLogger;

import java.util.concurrent.LinkedBlockingQueue;

public class StubLogger extends SubstituteLogger {
    public int calledDebug = 0;
    public int calledInfo = 0;
    public int calledWarn = 0;
    public int calledError = 0;
    public Throwable lastThrowable = null;

    public StubLogger() {
        super(StubLogger.class.getName(), new LinkedBlockingQueue<>(), false);
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
