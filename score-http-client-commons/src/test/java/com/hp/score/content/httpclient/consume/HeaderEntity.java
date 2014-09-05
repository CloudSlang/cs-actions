package com.hp.score.content.httpclient.consume;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
public class HeaderEntity implements Header {

    private String name;
    private String value;

    protected HeaderEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        return new HeaderElement[0];
    }

    @Override
    public String toString() {
        return name + ":" + value;
    }
}