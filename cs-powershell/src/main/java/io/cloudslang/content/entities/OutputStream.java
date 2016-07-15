package io.cloudslang.content.entities;

/**
 * Created by giloan on 6/7/2016.
 */
public enum OutputStream {
    STDOUT("stdout"),
    STDERR("stderr");

    private String value;

    OutputStream(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
