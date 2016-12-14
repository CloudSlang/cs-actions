/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.rft.entities;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class KeyFile {
    private String keyFilePath;
    private String passPhrase;

    public KeyFile(String keyFilePath) {
        this.keyFilePath = keyFilePath;
        this.passPhrase = null;
    }

    public KeyFile(String keyFilePath, String passPhrase) {
        this.keyFilePath = keyFilePath;
        this.passPhrase = passPhrase;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

}
