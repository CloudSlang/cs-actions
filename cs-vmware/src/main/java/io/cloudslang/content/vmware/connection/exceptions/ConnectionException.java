/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.connection.exceptions;

/**
 * Created by Mihai Tusa.
 * 10/20/2015.
 */
public class ConnectionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
