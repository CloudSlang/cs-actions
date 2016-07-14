/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.httpclient.consume;

import io.cloudslang.content.httpclient.CSHttpClient;
import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class FinalLocationConsumer {
    private URI uri;
    private HttpHost targetHost;
    private List<URI> redirectLocations;

    public FinalLocationConsumer setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public FinalLocationConsumer setTargetHost(HttpHost targetHost) {
        this.targetHost = targetHost;
        return this;
    }

    public FinalLocationConsumer setRedirectLocations(List<URI> redirectLocations) {
        this.redirectLocations = redirectLocations;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        URI location;
        try {
            location = URIUtils.resolve(uri, targetHost, redirectLocations);
        } catch (URISyntaxException e) {
            //this is not a fatal error
            throw new IllegalArgumentException("could not determine '" + CSHttpClient.FINAL_LOCATION
                    + "': " + e.getMessage(), e);
        }
        returnResult.put(CSHttpClient.FINAL_LOCATION, location.toASCIIString());
    }
}