package io.cloudslang.content.json.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Folea Ilie Cristian on 2/5/2016.
 */
public class StringUtils {
    public static String toString(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString().replace("\u0000", "");
    }
}
