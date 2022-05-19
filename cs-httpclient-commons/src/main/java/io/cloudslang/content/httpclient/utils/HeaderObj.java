package io.cloudslang.content.httpclient.utils;

import org.apache.hc.core5.http.*;

public class HeaderObj implements Header {

    private String name;
    private String value;

    public HeaderObj(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
