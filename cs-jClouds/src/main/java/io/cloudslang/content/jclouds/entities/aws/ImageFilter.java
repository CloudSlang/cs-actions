package io.cloudslang.content.jclouds.entities.aws;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public enum ImageFilter {
    ID("image-id"),
    TYPE("image-type"),
    DESCRIPTION("description"),
    IS_PUBLIC("is-public"),
    MANIFEST_LOCATION("manifest-location"),
    NAME("name"),
    STATE("state");

    private String value;

    ImageFilter(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
