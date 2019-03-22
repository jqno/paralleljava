package nl.jqno.paralleljava.app.controller;

import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.persistence.IdGenerator;
import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.serialization.Serializer;

import java.util.UUID;

public class DefaultController implements Controller {
    private final String url;
    private final Repository repository;
    private final IdGenerator generator;
    private final Serializer serializer;
    private final Logger logger;

    public DefaultController(String url, Repository repository, IdGenerator generator, Serializer serializer, LoggerFactory loggerFactory) {
        this.url = url;
        this.repository = repository;
        this.generator = generator;
        this.serializer = serializer;
        this.logger = loggerFactory.create(getClass());
    }

    public String get() {
        return serializer.serializeTodos(repository.getAllTodos());
    }

    public String get(String id) {
        return serializer.deserializeUuid(id)
                .flatMap(repository::get)
                .map(serializer::serializeTodo)
                .getOrElse("");
    }

    public String post(String json) {
        logger.forProduction("POSTed: " + json);
        var partialTodo = serializer.deserializePartialTodo(json);
        if (partialTodo.isDefined() && partialTodo.get().title().isDefined()) {
            var pt = partialTodo.get();
            var id = generator.generateId();
            var todo = new Todo(id, pt.title().get(), buildUrlFor(id), false, pt.order().getOrElse(0));
            repository.createTodo(todo);
            logger.forProduction("Returning from POST: " + json);
            return serializer.serializeTodo(todo);
        }
        else {
            return "";
        }
    }

    public String patch(String id, String json) {
        logger.forProduction("PATCHed: " + json);
        var uuid = serializer.deserializeUuid(id);
        var partialTodo = serializer.deserializePartialTodo(json);
        if (uuid.isDefined() && partialTodo.isDefined()) {
            var pt = partialTodo.get();
            var todo = repository.get(uuid.get());
            if (todo.isDefined()) {
                var todo0 = todo.get();
                var todo1 = pt.title().map(todo0::withTitle).getOrElse(todo0);
                var todo2 = pt.completed().map(todo1::withCompleted).getOrElse(todo1);
                var todo3 = pt.order().map(todo2::withOrder).getOrElse(todo2);
                repository.updateTodo(todo3);
                return serializer.serializeTodo(todo3);
            }
            return "";
        }
        else {
            return "";
        }
    }

    public String delete() {
        repository.clearAllTodos();
        return "";
    }

    public String delete(String id) {
        var uuid = serializer.deserializeUuid(id);
        if (uuid.isDefined()) {
            repository.delete(uuid.get());
        }
        return "";
    }

    private String buildUrlFor(UUID id) {
        return url + "/" + id.toString();
    }
}
