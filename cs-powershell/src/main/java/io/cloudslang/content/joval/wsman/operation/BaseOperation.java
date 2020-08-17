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

import io.cloudslang.content.joval.Constants;
import io.cloudslang.content.joval.wsman.FaultException;
import io.cloudslang.content.joval.wsman.Port;
import org.dmtf.wsman.AttributableDuration;
import org.dmtf.wsman.AttributableURI;
import org.dmtf.wsman.SelectorSetType;

import javax.security.auth.login.FailedLoginException;
import javax.xml.bind.JAXBException;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Base class for all WSMV operations.
 *
 * @author David A. Solin
 * @version %I%, %G%
 */
abstract class BaseOperation<I, O> implements IOperation<I, O>, Constants {
    String action;
    List<Object> headers;
    AttributableDuration duration = null;
    I input;

    public BaseOperation(String action, I input) {
	this.action = action;
	this.input = input;
	headers = new Vector<Object>();
    }

    public void addHeader(Object obj) {
	headers.add(obj);
    }

    public void addSelectorSet(SelectorSetType selectors) {
	headers.add(Factories.WSMAN.createSelectorSet(selectors));
    }

    // Implement IOperation

    public void setTimeout(long millis) {
	if (duration == null) {
	    duration = Factories.WSMAN.createAttributableDuration();
	}
	duration.setValue(Factories.XMLDT.newDuration(millis));
    }

    public void addResourceURI(String str) {
	AttributableURI uri = Factories.WSMAN.createAttributableURI();
	uri.setValue(str);
	uri.getOtherAttributes().put(MUST_UNDERSTAND, "true");
	headers.add(Factories.WSMAN.createResourceURI(uri));
    }

    public O dispatch(Port port) throws IOException, HTTPException, JAXBException, FaultException, FailedLoginException {
	@SuppressWarnings("unchecked")
        O result = (O)dispatch0(port);
	return result;
    }

    // Internal

    /**
     * The internal implementation of dispatch, which subclasses that override the typed public dispatch method can use.
     */
    final Object dispatch0(Port port) throws IOException, HTTPException, JAXBException, FaultException, FailedLoginException {
	List<Object> dispatchHeaders = new Vector<Object>();
	dispatchHeaders.addAll(headers);
	if (duration != null) {
	    dispatchHeaders.add(Factories.WSMAN.createOperationTimeout(duration));
	}
	return port.dispatch(action, dispatchHeaders, input);
    }
}
