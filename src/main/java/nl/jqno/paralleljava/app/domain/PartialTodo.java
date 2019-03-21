package nl.jqno.paralleljava.app.domain;

import io.vavr.control.Option;

import java.util.Objects;

public final class PartialTodo {
    private final Integer id;
    private final String title;
    private final String url;
    private final Boolean completed;
    private final int order;

    public PartialTodo(Option<Integer> id, String title, Option<String> url, Option<Boolean> completed, int order) {
        this.id = id.getOrNull();
        this.title = title;
        this.url = url.getOrNull();
        this.completed = completed.getOrNull();
        this.order = order;
    }

    public Option<Integer> id() {
        return Option.of(id);
    }

    public String title() {
        return title;
    }

    public Option<String> url() {
        return Option.of(url);
    }

    public Option<Boolean> completed() {
        return Option.of(completed);
    }

    public int order() {
        return order;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PartialTodo)) {
            return false;
        }
        PartialTodo other = (PartialTodo)obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(title, other.title) &&
                Objects.equals(url, other.url) &&
                Objects.equals(completed, other.completed) &&
                order == other.order;
    }

    public int hashCode() {
        return Objects.hash(id, title, url, completed, order);
    }

    public String toString() {
        return "PartialTodo: [id=" + id() + ", title=" + title() + ", url=" + url() + ", completed=" + completed() + ", order=" + order() + "]";
    }
}
