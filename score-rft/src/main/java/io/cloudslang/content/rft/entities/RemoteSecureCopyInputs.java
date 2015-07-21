/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;

import java.util.Map;

/**
 * Date: 4/10/2015
 *
 * @author lesant
 */

public class RemoteSecureCopyInputs {
    private String sourceHost;
    private String sourcePath;
    private String sourcePort;
    private String sourceUsername;
    private String sourcePassword;
    private String sourcePrivateKeyFile;
    private String destinationHost;
    private String destinationPath;
    private String destinatinPort;
    private String destinationUsername;
    private String destinationPassword;
    private String destinationPrivateKeyFile;
    private String knownHostsPolicy;
    private String knownHostsPath;
    private String timeout;

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public void setDestinationUsername(String destinationUsername) {
        this.destinationUsername = destinationUsername;
    }

    public String getDestinationPrivateKeyFile() {
        return destinationPrivateKeyFile;
    }

    public void setDestinationPrivateKeyFile(String destinationPrivateKeyFile) {
        this.destinationPrivateKeyFile = destinationPrivateKeyFile;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }

    public String getSourcePassword() {
        return sourcePassword;
    }

    public void setSourcePassword(String sourcePassword) {
        this.sourcePassword = sourcePassword;
    }

    public String getSourcePrivateKeyFile() {
        return sourcePrivateKeyFile;
    }

    public void setSourcePrivateKeyFile(String sourcePrivateKeyFile) {
        this.sourcePrivateKeyFile = sourcePrivateKeyFile;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(String destinationHost) {
        this.destinationHost = destinationHost;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public String getDestinatinPort() {
        return destinatinPort;
    }

    public void setDestinatinPort(String destinatinPort) {
        this.destinatinPort = destinatinPort;
    }

    public String getDestinationPassword() {
        return destinationPassword;
    }

    public void setDestinationPassword(String destinationPassword) {
        this.destinationPassword = destinationPassword;
    }

    public String getKnownHostsPath() {
        return knownHostsPath;
    }

    public void setKnownHostsPath(String knownHostsPath) {
        this.knownHostsPath = knownHostsPath;
    }

    public String getKnownHostsPolicy() {
        return knownHostsPolicy;
    }

    public void setKnownHostsPolicy(String knownHostsPolicy) {
        this.knownHostsPolicy = knownHostsPolicy;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

}