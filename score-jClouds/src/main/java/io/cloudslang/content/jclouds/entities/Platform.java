package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/6/2016.
 */
public enum Platform {
    OTHERS(""),
    WINDOWS("windows");

    private String value;

    Platform(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        for (Platform member : Platform.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Invalid platform value: [" + input + "]. Valid values: \"\" (empty string), windows.");
    }
}
