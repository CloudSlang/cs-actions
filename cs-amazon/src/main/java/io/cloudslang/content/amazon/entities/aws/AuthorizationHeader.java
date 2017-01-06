/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

/**
 * Created by Mihai Tusa.
 * 8/8/2016.
 */
public class AuthorizationHeader {
    private final String signedHeaders;
    private final String signature;

    public AuthorizationHeader(String signedHeaders, String signature) {
        this.signedHeaders = signedHeaders;
        this.signature = signature;
    }

    public String getAuthorizationHeader() {
        return signedHeaders;
    }

    public String getSignature() {
        return signature;
    }
}
