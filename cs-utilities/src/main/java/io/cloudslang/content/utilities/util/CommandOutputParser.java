package io.cloudslang.content.utilities.util;

public class CommandOutputParser {

    public static String extractValue(String output, String key) {
        String value = "";
        int startIndex = output.indexOf(key);

        if (startIndex >= 0) {
            value = output.substring(startIndex + key.length());
        }

        return value;
    }

    public static String extractValue(String output, String key, String endTag) {
        String value = "";
        int startIndex = output.indexOf(key);
        int valueStartIndex = startIndex + key.length();

        if (startIndex >= 0) {
            value = output.substring(valueStartIndex, output.indexOf(endTag, valueStartIndex));
        }

        return value;
    }
}
