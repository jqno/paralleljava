package nl.jqno.paralleljava.app.persistence;

import java.util.UUID;

public interface IdGenerator {
    UUID generateId();
}
