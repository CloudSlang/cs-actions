/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.httpclient.build;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 8/28/14
 */
public class RequestBuilderTest {
    @Test(expected = IllegalArgumentException.class)
    public void testNoMethod() throws URISyntaxException {
        new io.cloudslang.content.httpclient.build.RequestBuilder().setUri(new URI("/")).build();
    }

    @Test
    public void testMethods() throws URISyntaxException {
        HttpRequestBase httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("GET").build();
        assertEquals(httpRequestBase.getMethod(), "GET");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("POST").build();
        assertEquals(httpRequestBase.getMethod(), "POST");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("PUT").build();
        assertEquals(httpRequestBase.getMethod(), "PUT");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("DELETE").build();
        assertEquals(httpRequestBase.getMethod(), "DELETE");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("TRACE").build();
        assertEquals(httpRequestBase.getMethod(), "TRACE");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("OPTIONS").build();
        assertEquals(httpRequestBase.getMethod(), "OPTIONS");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("HEAD").build();
        assertEquals(httpRequestBase.getMethod(), "HEAD");

        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("get").build();
        assertEquals(httpRequestBase.getMethod(), "GET");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("post").build();
        assertEquals(httpRequestBase.getMethod(), "POST");
        httpRequestBase = new RequestBuilder().setUri(new URI("/")).setMethod("put").build();
        assertEquals(httpRequestBase.getMethod(), "PUT");
    }

    @Test
    public void testEntity() throws URISyntaxException, IOException {
        HttpEntityEnclosingRequestBase httpRequestBase = (HttpEntityEnclosingRequestBase) new RequestBuilder().setUri(new URI("https://localhost:443/lalal?dd=3")).setMethod("POST").setEntity(
                new StringEntity("my custom entity")).build();
        assertEquals(IOUtils.toString(httpRequestBase.getEntity().getContent()), "my custom entity");
    }
}
