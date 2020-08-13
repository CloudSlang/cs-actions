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

package io.cloudslang.content.joval.wsman;

import com.microsoft.wsman.fault.ProviderFaultType;
import com.microsoft.wsman.fault.WSManFaultType;
import org.w3c.soap.envelope.Fault;

import javax.xml.bind.JAXBElement;

/**
 * A Web-Service (SOAP) Fault-derived exception class
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class FaultException extends Exception {
    Fault fault;

    public FaultException(Fault fault) {
	super();
	this.fault = fault;
    }

    public Fault getFault() {
	return fault;
    }

    /**
     * Extract the (required) detail message from the fault.
     */
    @Override
    public String getMessage() {
	if (fault.isSetDetail() && fault.getDetail().isSetAny()) {
	    StringBuffer sb = new StringBuffer();
	    for (Object obj : fault.getDetail().getAny()) {
		if (obj instanceof JAXBElement) {
		    obj = ((JAXBElement)obj).getValue();
		}
		if (obj instanceof WSManFaultType) {
		    return toString((WSManFaultType)obj);
		} else if (obj != null) {
		    sb.append(obj.toString());
		}
	    }
	    return sb.toString();
	} else {
	    return null;
	}
    }

    // Private

    private String toString(WSManFaultType wsmft) {
	StringBuffer sb = new StringBuffer();
	if (wsmft.isSetMessage() && wsmft.getMessage().isSetContent()) {
	    for (Object obj : wsmft.getMessage().getContent()) {
		if (obj instanceof JAXBElement) {
		    obj = ((JAXBElement)obj).getValue();
		}
		if (obj instanceof ProviderFaultType) {
		    ProviderFaultType pft = (ProviderFaultType)obj;
		    if (pft.isSetProviderId()) {
			sb.append("Provider: ").append(pft.getProviderId());
		    }
		    if (pft.isSetContent()) {
			for (Object content : pft.getContent()) {
			    if (content instanceof JAXBElement) {
				content = ((JAXBElement)content).getValue();
			    }
			    if (content instanceof WSManFaultType) {
				sb.append(toString((WSManFaultType)content));
			    }
			}
		    }
		} else if (obj != null) {
		    sb.append(obj.toString().trim());
		}
	    }
	}
	if (wsmft.isSetCode()) {
	    String code = new StringBuffer("Code: ").append(Long.toString(wsmft.getCode())).toString();
	    if (sb.length() == 0) {
		sb.append(code);
	    } else if (sb.indexOf(code) == -1) {
		sb.append(" [").append(code).append("]");
	    }
	}
	return sb.toString();
    }
}
