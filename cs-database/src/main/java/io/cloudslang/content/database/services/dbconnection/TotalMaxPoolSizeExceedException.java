/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services.dbconnection;

import java.sql.SQLException;

/**
 * class for handling exception when total max pool size exceeded.
 *
 * @author ggu
 */
public class TotalMaxPoolSizeExceedException extends SQLException {
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     *
     * @param aMsg a message
     */
    public TotalMaxPoolSizeExceedException(String aMsg) {
        super(aMsg);
    }

    /**
     * constructor
     *
     * @param aThrowable a Throwable
     */
    public TotalMaxPoolSizeExceedException(Throwable aThrowable) {
        super(aThrowable);
    }

    /**
     * constructor
     *
     * @param aMsg       a message
     * @param aThrowable a Throwable
     */
    public TotalMaxPoolSizeExceedException(String aMsg, Throwable aThrowable) {
        super(aMsg, aThrowable);
    }
}
