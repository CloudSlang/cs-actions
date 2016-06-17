package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public enum ImageFilters {
    ID("image-id"),
    TYPE("image-type"),
    DESCRIPTION("description"),
    IS_PUBLIC("is-public"),
    MANIFEST_LOCATION("manifest-location"),
    NAME("name");

    private String value;

    ImageFilters(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}