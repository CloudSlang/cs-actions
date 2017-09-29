/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.rft.entities;

/**
 * Date: 4/10/2015
 *
 * @author lesant
 */

public class RemoteSecureCopyInputs {
    private String srcHost;
    private String srcPath;
    private String srcPort;
    private String srcUsername;
    private String srcPassword;
    private String srcPrivateKeyFile;
    private String destHost;
    private String destPath;
    private String destPort;
    private String destUsername;
    private String destPassword;
    private String destPrivateKeyFile;
    private String knownHostsPolicy;
    private String knownHostsPath;
    private String timeout;

    public RemoteSecureCopyInputs(String srcPath, String destHost, String destPath, String destUsername) {
        this.srcPath = srcPath;
        this.destHost = destHost;
        this.destPath = destPath;
        this.destUsername = destUsername;
    }

    public String getDestUsername() {
        return destUsername;
    }

    public void setDestUsername(String destUsername) {
        this.destUsername = destUsername;
    }

    public String getDestPrivateKeyFile() {
        return destPrivateKeyFile;
    }

    public void setDestPrivateKeyFile(String destPrivateKeyFile) {
        this.destPrivateKeyFile = destPrivateKeyFile;
    }

    public String getSrcHost() {
        return srcHost;
    }

    public void setSrcHost(String srcHost) {
        this.srcHost = srcHost;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(String srcPort) {
        this.srcPort = srcPort;
    }

    public String getSrcUsername() {
        return srcUsername;
    }

    public void setSrcUsername(String srcUsername) {
        this.srcUsername = srcUsername;
    }

    public String getSrcPassword() {
        return srcPassword;
    }

    public void setSrcPassword(String srcPassword) {
        this.srcPassword = srcPassword;
    }

    public String getSrcPrivateKeyFile() {
        return srcPrivateKeyFile;
    }

    public void setSrcPrivateKeyFile(String srcPrivateKeyFile) {
        this.srcPrivateKeyFile = srcPrivateKeyFile;
    }

    public String getDestHost() {
        return destHost;
    }

    public void setDestHost(String destHost) {
        this.destHost = destHost;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getDestPort() {
        return destPort;
    }

    public void setDestPort(String destPort) {
        this.destPort = destPort;
    }

    public String getDestPassword() {
        return destPassword;
    }

    public void setDestPassword(String destPassword) {
        this.destPassword = destPassword;
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