package nl.jqno.paralleljava.app.environment;

import io.vavr.control.Option;

public interface Environment {

    int DEFAULT_PORT = 4567;
    String DEFAULT_URL = "http://localhost";
    String ENDPOINT = "/todo";
    String DEFAULT_JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    Option<Integer> port();
    Option<String> hostUrl();
    Option<String> jdbcUrl();
}
