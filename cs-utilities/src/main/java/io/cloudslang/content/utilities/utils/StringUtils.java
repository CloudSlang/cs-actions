/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utilities.utils;

import java.io.StringWriter;

/**
 * Created by persdana on 10/30/2014.
 */
public class StringUtils {
    /**
     * Scrubs an Exception stacktrace??
     *
     * @param e Exception to dump the stacktrace from
     * @return Stacktrace with the first NULL terminator removed
     */
    public static String toString(Throwable e) {
        // Print the stack trace into an in memory string
        StringWriter writer = new StringWriter();
        e.printStackTrace(new java.io.PrintWriter(writer));

        // Process the stack trace, remove the FIRST null character
        return writer.toString().replace("" + (char) 0x00, "");
    }
}
