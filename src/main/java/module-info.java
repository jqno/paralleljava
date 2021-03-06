module nl.jqno.paralleljava {
    exports nl.jqno.paralleljava;

    opens nl.jqno.paralleljava.app.domain to gson;

    requires io.vavr;
    requires io.vavr.gson;
    requires jdbi3.core;
    requires jdbi3.vavr;
    requires java.sql; // required for gson
    requires gson;
    requires slf4j.api;
    requires spark.core;
}