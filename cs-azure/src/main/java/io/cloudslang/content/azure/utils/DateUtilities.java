/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.azure.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by victor on 28.09.2016.
 */
public final class DateUtilities {
    @NotNull
    public static String formatDate(@NotNull final Date date) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
        return dateFormat.format(date);
    }

    @NotNull
    public static Date parseDate(@NotNull final String date) throws ParseException {
        return DateUtils.parseDate(date, "MM/dd/yyyy h:mm a");
    }
}
