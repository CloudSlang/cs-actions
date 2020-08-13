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

import com.microsoft.wsman.shell.CommandLine;
import com.microsoft.wsman.shell.CommandResponse;

import javax.xml.bind.JAXBElement;

/**
 * Command operation implementation class.
 *
 * @author David A. Solin
 * @version %I%, %G%
 */
public class CommandOperation extends BaseOperation<JAXBElement<CommandLine>, CommandResponse> {
    public CommandOperation(CommandLine input) {
	super("http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Command", Factories.SHELL.createCommandLine(input));
    }
}
