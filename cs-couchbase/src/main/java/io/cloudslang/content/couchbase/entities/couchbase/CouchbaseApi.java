/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.entities.couchbase;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public enum CouchbaseApi {
    BUCKETS("/pools/default/buckets");

    private final String value;

    CouchbaseApi(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}