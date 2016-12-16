/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.ssh.entities;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class CommandResult {
    private String standardOutput;
    private String standardError;
    private int exitCode;

    public CommandResult() {
    }

    public String getStandardOutput() {
        return standardOutput;
    }

    public void setStandardOutput(String standardOutput) {
        this.standardOutput = standardOutput;
    }

    public String getStandardError() {
        return standardError;
    }

    public void setStandardError(String standardError) {
        this.standardError = standardError;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "standardOutput='" + standardOutput + '\'' +
                ", standardError='" + standardError + '\'' +
                ", exitCode=" + exitCode +
                '}';
    }

}
