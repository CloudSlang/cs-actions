/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
// Copyright (C) 2012 jOVAL.org.  All rights reserved.
// This software is licensed under the AGPL 3.0 license available at http://www.joval.org/agpl_v3.txt

package io.cloudslang.content.joval.wsman.operation;


import io.cloudslang.content.joval.wsman.FaultException;
import io.cloudslang.content.joval.wsman.Port;

import javax.security.auth.login.FailedLoginException;
import javax.xml.bind.JAXBException;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * Interface describing a SOAP operation, with generic input and output types.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public interface IOperation<I, O> {
    /**
     * Set the timeout for the operation.
     */
    void setTimeout(long millis);

    /**
     * Add a mandatory DMTF WS-Management Resource URI header field for the operation.
     */
    void addResourceURI(String uri);

    /**
     * Get the output (i.e., SOAP response message body contents) for the operation, dispatched through the specified
     * port.
     */
    O dispatch(Port port) throws IOException, HTTPException, FailedLoginException, JAXBException, FaultException;
}
