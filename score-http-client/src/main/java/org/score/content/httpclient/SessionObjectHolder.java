package org.score.content.httpclient;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/25/14
 */
public class SessionObjectHolder<T> {
    private T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public void release() {
        this.object = null;
    }
}
