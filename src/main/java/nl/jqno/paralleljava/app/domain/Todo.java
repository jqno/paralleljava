package nl.jqno.paralleljava.app.domain;

import java.util.Objects;
import java.util.UUID;

public final class Todo {
    private final UUID id;
    private final String title;
    private final String url;
    private final boolean completed;
    private final int order;

    public Todo(UUID id, String title, String url, boolean completed, int order) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.completed = completed;
        this.order = order;
    }

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String url() {
        return url;
    }

    public boolean completed() {
        return completed;
    }

    public int order() {
        return order;
    }

    public Todo withTitle(String title) {
        return new Todo(id(), title, url(), completed(), order());
    }

    public Todo withCompleted(boolean completed) {
        return new Todo(id(), title(), url(), completed, order());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Todo)) {
            return false;
        }
        Todo other = (Todo)obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(title, other.title) &&
                Objects.equals(url, other.url) &&
                completed == other.completed &&
                order == other.order;
    }

    public int hashCode() {
        return Objects.hash(id, title, url, completed, order);
    }

    public String toString() {
        return "Todo: [id=" + id + ", title=" + title + ", url=" + url + ", completed=" + completed + ", order=" + order + "]";
    }
}
