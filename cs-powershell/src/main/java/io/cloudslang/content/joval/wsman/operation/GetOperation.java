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
import org.xmlsoap.ws.transfer.AnyXmlOptionalType;
import org.xmlsoap.ws.transfer.AnyXmlType;

import javax.security.auth.login.FailedLoginException;
import javax.xml.bind.JAXBException;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * Get operation implementation class.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class GetOperation extends BaseOperation<AnyXmlOptionalType, AnyXmlType> {
    public GetOperation(AnyXmlOptionalType input) {
	super("http://schemas.xmlsoap.org/ws/2004/09/transfer/Get", input);
    }

    @Override
    public AnyXmlType dispatch(Port port)
		throws IOException, HTTPException, JAXBException, FaultException, FailedLoginException {

	Object obj = dispatch0(port);
	if (obj instanceof AnyXmlType) {
	    return (AnyXmlType)obj;
	} else {
	    AnyXmlType any = Factories.TRANSFER.createAnyXmlType();
	    any.setAny(obj);
	    return any;
	}
    }
}
