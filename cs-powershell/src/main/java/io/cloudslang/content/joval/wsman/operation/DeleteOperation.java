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

import javax.security.auth.login.FailedLoginException;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Delete operation implementation class.
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class DeleteOperation extends BaseOperation<Object, AnyXmlOptionalType> {
    public DeleteOperation() {
	super("http://schemas.xmlsoap.org/ws/2004/09/transfer/Delete", null);
    }

    @Override
    public AnyXmlOptionalType dispatch(Port port) throws IOException, JAXBException, FaultException, FailedLoginException {
	Object obj = dispatch0(port);
	if (obj instanceof AnyXmlOptionalType) {
	    return (AnyXmlOptionalType)obj;
	} else {
	    AnyXmlOptionalType any = Factories.TRANSFER.createAnyXmlOptionalType();
	    if (obj != null) {
		any.setAny(obj);
	    }
	    return any;
	}
    }
}
