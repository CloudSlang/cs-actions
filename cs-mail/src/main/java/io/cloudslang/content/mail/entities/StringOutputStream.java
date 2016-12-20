/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.mail.entities;

/**
 * Created by giloan on 11/5/2014.
 */
public class StringOutputStream extends java.io.OutputStream {
    String written;

    public StringOutputStream() {
        written = "";
    }

    public void write(int val) {
        written += (char) val;
    }

    public void write(byte[] buff) {
        written += new String(buff);
    }

    public void write(byte[] buff, int offset, int len) {
        written += new String(buff, offset, len);
    }

    public String toString() {
        return written;
    }

}
