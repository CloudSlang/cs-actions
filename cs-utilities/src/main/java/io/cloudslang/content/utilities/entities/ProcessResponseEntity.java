/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.entities;

public class ProcessResponseEntity {
    private final String stdout;
    private final String stderr;
    private final int exitCode;
    private final boolean timeout;

    public ProcessResponseEntity(final String stdout, final String stderr, final int exitCode, boolean timeout) {
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
        this.timeout = timeout;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public int getExitCode() {
        return exitCode;
    }
}
