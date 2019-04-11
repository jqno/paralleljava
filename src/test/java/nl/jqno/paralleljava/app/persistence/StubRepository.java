package nl.jqno.paralleljava.app.persistence;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import nl.jqno.paralleljava.app.domain.Todo;

import java.util.UUID;

public class StubRepository implements Repository {

    public boolean failNextCall = false;
    public int calledInitialize = 0;
    public int calledCreate = 0;
    public int calledGet = 0;
    public int calledGetAll = 0;
    public int calledUpdateDirectly = 0;
    public int calledUpdateModify = 0;
    public int calledDelete = 0;
    public int calledDeleteAll = 0;

    public void clear() {
        failNextCall = false;
        calledInitialize = 0;
        calledCreate = 0;
        calledGet = 0;
        calledGetAll = 0;
        calledUpdateDirectly = 0;
        calledUpdateModify = 0;
        calledDelete = 0;
        calledDeleteAll = 0;
    }

    public Try<Void> initialize() {
        calledInitialize += 1;
        return returnValue(null);
    }

    public Try<Void> create(Todo todo) {
        calledCreate += 1;
        return returnValue(null);
    }

    public Try<Option<Todo>> get(UUID id) {
        calledGet += 1;
        return returnValue(Option.none());
    }

    public Try<List<Todo>> getAll() {
        calledGetAll += 1;
        return returnValue(List.empty());
    }

    public Try<Void> update(Todo todo) {
        calledUpdateDirectly += 1;
        return returnValue(null);
    }

    public Try<Todo> update(UUID id, Function1<Todo, Todo> f) {
        calledUpdateModify += 1;
        return returnValue(null);
    }

    public Try<Void> delete(UUID id) {
        calledDelete += 1;
        return returnValue(null);
    }

    public Try<Void> deleteAll() {
        calledDeleteAll += 1;
        return returnValue(null);
    }

    private <T> Try<T> returnValue(T value) {
        if (failNextCall) {
            failNextCall = false;
            return Try.failure(new IllegalStateException());
        }
        return Try.success(value);
    }
}
