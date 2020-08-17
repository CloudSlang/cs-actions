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

import jline.ConsoleReader;

import java.io.*;
import java.io.IOException;

/**
 * Uses a JLine ConsoleReader.
 */
public class TerminalInput implements Runnable {
    Thread thread;
    ConsoleReader reader;
    java.io.OutputStream out;

    public TerminalInput(ConsoleReader reader, java.io.OutputStream out) {
        this.reader = reader;
        this.out = out;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void close() throws IOException {
        thread.interrupt();
    }

    public void run() {
        try {
            String line = null;
            while((line = reader.readLine()) != null) {
                out.write(line.getBytes());
                out.write("\r\n".getBytes());
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
