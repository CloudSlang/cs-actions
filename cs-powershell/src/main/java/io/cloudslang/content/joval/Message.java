/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.joval;// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the AGPL 3.0 license available at http://www.joval.org/agpl_v3.txt

import ch.qos.cal10n.*;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

/**
 * Uses cal10n to define localized messages for jWSMV.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
@BaseName("jwsmvmsg")
@LocaleData(
  defaultCharset="ASCII",
  value = { @Locale("en_US")
          }
)
public enum Message {
    STATUS_CONNECT,
    STATUS_REQUEST,
    STATUS_RESPONSE,
    ERROR_MISSING_RESOURCE,
    ERROR_RESPONSE,
    ERROR_EXCEPTION;

    private static IMessageConveyor mc;
    private static LocLoggerFactory loggerFactory;
    private static LocLogger sysLogger;

    static {
	mc = new MessageConveyor(java.util.Locale.getDefault());
	try {
	    //
	    // Get a message to test whether localized messages are available for the default Locale
	    //
	    getMessage(Message.ERROR_EXCEPTION);
	} catch (MessageConveyorException e) {
	    //
	    // The test failed, so set the message Locale to English
	    //
	    mc = new MessageConveyor(java.util.Locale.ENGLISH);
	}
	loggerFactory = new LocLoggerFactory(mc);
	sysLogger = loggerFactory.getLocLogger(Message.class);
    }

    /**
     * Retrieve the logger used by the jWSMV library.
     */
    public static LocLogger getLogger() {
	return sysLogger;
    }

    /**
     * Get the IMessageConveyor that provides localized messages for jWSMV.
     */
    public static IMessageConveyor getConveyor() {
	return mc;
    }

    /**
     * Retrieve a localized String, given the key and substitution arguments. Convenience message for getConveyor.getMessage.
     */
    public static String getMessage(Message key, Object... args) {
	return mc.getMessage(key, args);
    }
}
