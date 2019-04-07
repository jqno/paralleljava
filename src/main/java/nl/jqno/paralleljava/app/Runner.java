package nl.jqno.paralleljava.app;

import nl.jqno.paralleljava.app.logging.Logger;
import nl.jqno.paralleljava.app.logging.LoggerFactory;
import nl.jqno.paralleljava.app.persistence.Repository;
import nl.jqno.paralleljava.app.server.Server;

public class Runner {
    private final Repository repository;
    private final Server server;
    private final Logger logger;

    public Runner(Repository repository, Server server, LoggerFactory loggerFactory) {
        this.repository = repository;
        this.server = server;
        this.logger = loggerFactory.create(getClass());
    }

    public void startup() {
        repository.initialize()
                .onSuccess(ignored -> {
                    printBanner();
                    server.run();
                });
    }

    private void printBanner() {
        logger.forProduction(" _   _         _____                                            _    _ __ _ _");
        logger.forProduction("| \\ | | ___   |  ___| __ __ _ _ __ ___   _____      _____  _ __| | _| |\\ \\ \\ \\");
        logger.forProduction("|  \\| |/ _ \\  | |_ | '__/ _` | '_ ` _ \\ / _ \\ \\ /\\ / / _ \\| '__| |/ / | \\ \\ \\ \\");
        logger.forProduction("| |\\  | (_) | |  _|| | | (_| | | | | | |  __/\\ V  V / (_) | |  |   <|_|  ) ) ) )");
        logger.forProduction("|_| \\_|\\___/  |_|  |_|  \\__,_|_| |_| |_|\\___| \\_/\\_/ \\___/|_|  |_|\\_(_) / / / /");
        logger.forProduction("=======================================================================/_/_/_/");
        logger.forProduction(" :: Built with Plain Java! ::                                          \uD83C\uDF89");
    }
}
