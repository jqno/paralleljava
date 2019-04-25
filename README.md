# Parallel Java

[![Build Status](https://img.shields.io/travis/jqno/paralleljava.svg?style=plastic)](https://travis-ci.org/jqno/paralleljava)

This app is written in Java from a Parallel Universe where annotations were never invented. You can check: there isn't a single annotation in this codebase!

It requires a JDK 11 to build and run.

It's a showcase for my talk, Java from a Parallel Universe. It's also a fully functioning [Todo Backend](https://www.todobackend.com/) (you can [run the Todo Backend test suite](https://www.todobackend.com/specs/index.html?https://parallel-java.herokuapp.com/todo
)!), using the [Spark web framework](http://sparkjava.com/), the [Jdbi database framework](http://jdbi.org/), and **no framework** for dependency injection because you really really don't need one. It also uses Java 11 `var` declarations, [Vavr](http://www.vavr.io/) and [Polyglot for Maven](https://github.com/takari/polyglot-maven) because I think they're pretty nifty and because they make the code look a little different, as if, I dunno, as if it came from a Parallel Universe or something? Also, the application is fully modularized and has 100% test coverage because why not.

Note, however, that this is still a demo app that is not production-ready. Some corners have definitely been cut. For example, the [InMemoryRepository](https://github.com/jqno/paralleljava/blob/master/src/main/java/nl/jqno/paralleljava/app/persistence/inmemory/InMemoryRepository.java) is not thread-safe.

