package nl.jqno.paralleljava.app;

import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.server.Server;

public class Runner {
    private final Repository repository;
    private final Server server;

    public Runner(Repository repository, Server server) {
        this.repository = repository;
        this.server = server;
    }

    public void startup() {
        repository.initialize()
                .onSuccess(ignored -> server.run());
    }
}
