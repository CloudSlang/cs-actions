/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
// Copyright (C) 2011 jOVAL.org.  All rights reserved.
// This software is licensed under the AGPL 3.0 license available at http://www.joval.org/agpl_v3.txt

package io.cloudslang.content.joval.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple utility for working with RFC822-formatted date strings.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class RFC822 {
    static final SimpleDateFormat formats[] = { new SimpleDateFormat("EEE, d MMM yy HH:mm:ss z"),
						new SimpleDateFormat("EEE, d MMM yy HH:mm z"),
						new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z"),
						new SimpleDateFormat("EEE, d MMM yyyy HH:mm z"),
						new SimpleDateFormat("d MMM yy HH:mm z"),
						new SimpleDateFormat("d MMM yy HH:mm:ss z"),
						new SimpleDateFormat("d MMM yyyy HH:mm z"),
						new SimpleDateFormat("d MMM yyyy HH:mm:ss z")
					      };

    /**
     * Convert the supplied RFC822-formatted date-time string into a millis-since-epoch timestamp.
     */
    public static final long valueOf(String s) throws IllegalArgumentException {
	for (SimpleDateFormat sdf : formats) {
	    try {
		return sdf.parse(s).getTime();
	    } catch (Exception e) {
	    }
	}
	throw new IllegalArgumentException(s);
    }

    public static final String toString(long tm) {
	return formats[0].format(new Date(tm));
    }
}
