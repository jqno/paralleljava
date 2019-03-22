package nl.jqno.paralleljava.app.controller;

import nl.jqno.paralleljava.app.domain.Todo;
import nl.jqno.paralleljava.app.logging.Logger;
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

    public DefaultController(String url, Repository repository, IdGenerator generator, Serializer serializer, Logger logger) {
        this.url = url;
        this.repository = repository;
        this.generator = generator;
        this.serializer = serializer;
        this.logger = logger;
    }

    public String get() {
        return serializer.serializeTodos(repository.getAllTodos());
    }

    public String get(String json) {
        return serializer.deserializeUuid(json)
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
            var todo = new Todo(id, pt.title().get(), buildUrlFor(id), false, 0);
            repository.createTodo(todo);
            logger.forProduction("Returning from POST: " + json);
            return serializer.serializeTodo(todo);
        }
        else {
            return "";
        }
    }

    public String delete() {
        repository.clearAllTodos();
        return "";
    }

    private String buildUrlFor(UUID id) {
        return url + "/" + id.toString();
    }
}
