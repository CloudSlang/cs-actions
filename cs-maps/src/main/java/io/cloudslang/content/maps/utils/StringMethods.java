package io.cloudslang.content.maps.utils;

public class StringMethods {

    public static String execute(String object, String method, String value) {

        switch (method.toLowerCase()) {
            case "to_uppercase":
                return object.toUpperCase();
            case "to_lowercase":
                return object.toLowerCase();
            case "add_prefix":
                return value + object;
            case "add_suffix":
                return object + value;
            default:
                return object.trim();
        }
    }
}
