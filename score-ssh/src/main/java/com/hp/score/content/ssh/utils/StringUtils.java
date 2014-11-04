package com.hp.score.content.ssh.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class StringUtils {

    public static boolean toBoolean(String value, boolean defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Boolean.valueOf(value);
    }

    public static int toInt(String value, int defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return Integer.valueOf(value);
    }

    public static String toNotEmptyString(String value, String defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }

        return value;
    }

    public static String toNotNullString(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    public static String toNewline(String value, String defaultValue) {
        value = toNotNullString(value, defaultValue);
        char[] chars;
        if (!value.matches("((\\d)|,)*")) {
            String[] split = value.split("\\\\");
            chars = new char[split.length - 1];
            for (int count = 0; count < split.length; count++)
                if (split[count].equalsIgnoreCase("n"))
                    chars[count - 1] = '\n';
                else if (split[count].equalsIgnoreCase("r"))
                    chars[count - 1] = '\r';
                else if (split[count].length() != 0) {
                    throw new RuntimeException("Unable to parse sequence of newline characters. Please specify this input as a comma delimited list of base-10 ASCII character codes.");
                }
        } else {
            String[] split = value.split(",");
            chars = new char[split.length];
            for (int count = 0; count < split.length; count++)
                chars[count] = (char) Integer.parseInt(split[count]);
        }
        return new String(chars);
    }

    public static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
}
