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

import java.io.IOException;

/**
 * A Java implementation of MS-XCA (LZ77) "Xpress" encoding.
 *
 * @see http://msdn.microsoft.com/en-us/library/ee896247%28v=prot.20%29.aspx
 *
 * @author David A. Solin
 * @version %I% %G%
 */
public class Xpress {
    public Xpress() {
	throw new RuntimeException("Xpress encoding is not implemented");
    }

    /**
     * Compress and encode.
     */
    public byte[] encode(byte[] buff) throws IOException {
	return buff;
    }

    /**
     * Decode and uncompress.
     */
    public byte[] decode(byte[] buff) throws IOException {
	return buff;
    }
}
