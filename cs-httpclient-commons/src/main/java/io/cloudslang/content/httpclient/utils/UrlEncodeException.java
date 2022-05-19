package io.cloudslang.content.httpclient.utils;

public class UrlEncodeException extends IllegalArgumentException {

    public UrlEncodeException(String message, RuntimeException e) {
        super(message, e);
    }
}
