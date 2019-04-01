package nl.jqno.paralleljava.app.persistence.database;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;

public interface Jdbi {
    <X extends Exception> Try<Void> execute(HandleConsumer<X> consumer);
    <T, X extends Exception> Try<T> query(HandleCallback<T, X> callback);
}
