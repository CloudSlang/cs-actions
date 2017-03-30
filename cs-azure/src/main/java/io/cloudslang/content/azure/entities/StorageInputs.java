/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.entities;

/**
 * Created by victor on 09.11.2016.
 */
public class StorageInputs {
    private final String storageAccount;
    private final String key;
    private final String containerName;
    private final String blobName;
    private final String proxyHost;
    private final int proxyPort;
    private final String proxyUsername;
    private final String  proxyPassword;
    private final int timeout;

    @java.beans.ConstructorProperties({"storageAccount", "key", "containerName", "blobName", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword", "timeout"})
    StorageInputs(final String storageAccount, final String key, final String containerName, final String blobName, final String proxyHost, final int proxyPort, final String proxyUsername, final String proxyPassword, final int timeout) {
        this.storageAccount = storageAccount;
        this.key = key;
        this.containerName = containerName;
        this.blobName = blobName;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.timeout = timeout;
    }

    public static StorageInputsBuilder builder() {
        return new StorageInputsBuilder();
    }

    public String getStorageAccount() {
        return this.storageAccount;
    }

    public String getKey() {
        return this.key;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public String getBlobName() {
        return this.blobName;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public String getProxyUsername() {
        return this.proxyUsername;
    }

    public String getProxyPassword() {
        return this.proxyPassword;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public static class StorageInputsBuilder {
        private String storageAccount;
        private String key;
        private String containerName;
        private String blobName;
        private String proxyHost;
        private int proxyPort = 8080;
        private String proxyUsername;
        private String proxyPassword;
        private int timeout = 0;

        StorageInputsBuilder() {
        }

        public StorageInputsBuilder storageAccount(String storageAccount) {
            this.storageAccount = storageAccount;
            return this;
        }

        public StorageInputsBuilder key(String key) {
            this.key = key;
            return this;
        }

        public StorageInputsBuilder containerName(String containerName) {
            this.containerName = containerName;
            return this;
        }

        public StorageInputsBuilder blobName(String blobName) {
            this.blobName = blobName;
            return this;
        }

        public StorageInputsBuilder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public StorageInputsBuilder proxyPort(int proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public StorageInputsBuilder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public StorageInputsBuilder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public StorageInputsBuilder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public StorageInputs build() {
            return new StorageInputs(storageAccount, key, containerName, blobName, proxyHost, proxyPort, proxyUsername, proxyPassword, timeout);
        }

        public String toString() {
            return "StorageInputsBuilder(storageAccount=" + this.storageAccount + ", key=" + this.key + ", containerName=" + this.containerName + ", blobName=" + this.blobName + ", proxyHost=" + this.proxyHost + ", proxyPort=" + this.proxyPort + ", proxyUsername=" + this.proxyUsername + ", proxyPassword=" + this.proxyPassword + ", timeout=" + this.timeout + ")";
        }
    }
}
