/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.ssh.entities;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class KeyFile extends IdentityKey {
    private String keyFilePath;

    public KeyFile(String keyFilePath) {
        this.keyFilePath = keyFilePath;
        this.passPhrase = null;
    }

    public KeyFile(String keyFilePath, String passPhrase) {
        this.keyFilePath = keyFilePath;
        this.setPassPhrase(passPhrase);
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }
}
