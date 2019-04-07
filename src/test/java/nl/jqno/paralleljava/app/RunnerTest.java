package nl.jqno.paralleljava.app;

import nl.jqno.paralleljava.app.logging.Slf4jLogger;
import nl.jqno.paralleljava.app.logging.StubLogger;
import nl.jqno.paralleljava.app.persistence.StubRepository;
import nl.jqno.paralleljava.app.server.StubServer;
import nl.jqno.picotest.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RunnerTest extends Test {

    public void runner() {
        var repo = new StubRepository();
        var server = new StubServer();
        var logger = new StubLogger();
        var runner = new Runner(repo, server, c -> new Slf4jLogger(logger));

        beforeEach(() -> {
            repo.clear();
            server.clear();
        });

        test("happy path", () -> {
            runner.startup();

            assertThat(repo.calledInitialize).isEqualTo(1);
            assertThat(server.calledRun).isEqualTo(1);
            assertThat(logger.calledInfo).isEqualTo(7);
        });

        test("repo initialization fails", () -> {
            repo.failNextCall = true;

            runner.startup();

            assertThat(repo.calledInitialize).isEqualTo(1);
            assertThat(server.calledRun).isEqualTo(0);
        });
    }
}
