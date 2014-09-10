package com.hp.score.content.httpclient.build;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 9/9/14
 */
public class UrlEncodeException extends IllegalArgumentException {

    public UrlEncodeException(String message, RuntimeException e) {
        super(message, e);
    }
}
