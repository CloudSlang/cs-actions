/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.entities;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple stream copier that polls the input for available data.
 */
public class StreamCopier implements Runnable {
    Thread thread;
    InputStream in;
    java.io.OutputStream out;
    boolean open;

    public StreamCopier(InputStream in, java.io.OutputStream out) {
        this.in = in;
        this.out = out;
        thread = new Thread(this);
        open = false;
    }

    public void start() {
        open = true;
        thread.start();
    }

    public void close() throws IOException {
        open = false;
    }

    public void run() {
        try {
            while(open) {
                int len = in.available();
                if (len > 0) {
                    byte[] buff = new byte[len];
                    in.read(buff);
                    out.write(buff);
                    out.flush();
                } else {
                    Thread.sleep(250);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}