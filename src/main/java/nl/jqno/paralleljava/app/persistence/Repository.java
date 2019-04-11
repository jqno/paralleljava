package nl.jqno.paralleljava.app.persistence;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public interface Repository {
    Try<Void> initialize();
    Try<Void> create(Todo todo);
    Try<Option<Todo>> get(UUID id);
    Try<List<Todo>> getAll();
    Try<Void> update(Todo todo);
    Try<Todo> update(UUID id, Function1<Todo, Todo> f);
    Try<Void> delete(UUID id);
    Try<Void> deleteAll();
}
