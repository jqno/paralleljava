package nl.jqno.paralleljava.app.controller;

import io.vavr.control.Try;
import nl.jqno.paralleljava.app.controller.Controller;

public class StubController implements Controller {

    private static final Try<String> SUCCESS = Try.success("");
    private static final Try<String> FAILURE_4xx = Try.failure(new IllegalArgumentException());
    private static final Try<String> FAILURE_5xx = Try.failure(new IllegalStateException());

    public boolean nextRequestFails4xx = false;
    public boolean nextRequestFails5xx = false;
    public int calledGet = 0;
    public int calledGetWithId = 0;
    public int calledPost = 0;
    public int calledPatchWithId = 0;
    public int calledDelete = 0;
    public int calledDeleteWithId = 0;

    public void clear() {
        nextRequestFails4xx = false;
        nextRequestFails5xx = false;
        calledGet = 0;
        calledGetWithId = 0;
        calledPost = 0;
        calledPatchWithId = 0;
        calledDelete = 0;
        calledDeleteWithId = 0;
    }

    public int calledTotal() {
        return calledGet + calledGetWithId + calledPost + calledPatchWithId + calledDelete + calledDeleteWithId;
    }

    public Try<String> get() {
        calledGet += 1;
        return response();
    }

    public Try<String> get(String id) {
        calledGetWithId += 1;
        return response();
    }

    public Try<String> post(String json) {
        calledPost += 1;
        return response();
    }

    public Try<String> patch(String id, String json) {
        calledPatchWithId += 1;
        return response();
    }

    public Try<String> delete() {
        calledDelete += 1;
        return response();
    }

    public Try<String> delete(String id) {
        calledDeleteWithId += 1;
        return response();
    }

    private Try<String> response() {
        if (nextRequestFails4xx) {
            return FAILURE_4xx;
        }
        if (nextRequestFails5xx) {
            return FAILURE_5xx;
        }
        return SUCCESS;
    }
}
