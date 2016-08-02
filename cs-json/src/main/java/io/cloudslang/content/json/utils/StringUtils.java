package io.cloudslang.content.json.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Folea Ilie Cristian on 2/5/2016.
 */
public class StringUtils {
    private static final String EMPTY_STRING = "";

    public static String getStackTraceAsString(Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            final String stackTraceAsString = sw.toString();
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.close();
            return stackTraceAsString;
        } else {
            return EMPTY_STRING;
        }
    }
    public static boolean isEmpty(Object val) {
        return (val == null)||(EMPTY_STRING.equals(val));
    }


}
