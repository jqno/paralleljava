module nl.jqno.paralleljava {
    exports nl.jqno.paralleljava;

    requires io.vavr;
    requires io.vavr.gson;
    requires java.sql; // required for gson
    requires gson;
    requires slf4j.api;
    requires spark.core;
}