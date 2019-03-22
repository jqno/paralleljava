package nl.jqno.paralleljava.app.logging;

import io.vavr.Function1;

public interface LoggerFactory extends Function1<Class<?>, Logger> {
    default Logger create(Class<?> c) {
        return apply(c);
    }
}
