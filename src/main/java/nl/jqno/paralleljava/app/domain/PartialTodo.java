package nl.jqno.paralleljava.app.domain;

import io.vavr.control.Option;

public class PartialTodo {
    private final Option<Integer> id;
    private final String title;
    private final Option<String> url;
    private final Option<Boolean> completed;
    private final int order;

    public PartialTodo(Option<Integer> id, String title, Option<String> url, Option<Boolean> completed, int order) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.completed = completed;
        this.order = order;
    }

    public Option<Integer> id() {
        return id;
    }

    public String title() {
        return title;
    }

    public Option<String> url() {
        return url;
    }

    public Option<Boolean> completed() {
        return completed;
    }

    public int order() {
        return order;
    }

    public String toString() {
        return "PartialTodo: [id=" + id + ", title=" + title + ", url=" + url + ", completed=" + completed + ", order=" + order + "]";
    }
}
