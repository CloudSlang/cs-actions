/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.content.httpclient.build;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openscore.content.httpclient.build.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/16/14
 */
public class URIBuilderTest {
    private static final String URL = "http://localhost:8002";
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void buildURIWithEncoding() throws URISyntaxException {
        String queryParams = "param 1=value1&param 2=value2";
        URI uri = new URIBuilder().setUrl(URL).setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param+1=value1&param+2=value2", uri.toString());
    }

    @Test
    public void buildURIWithEncoding1() throws URISyntaxException {
        String queryParams = "param 1=&param 2=";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("false").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param+1=&param+2=", uri.toString());
    }

    @Test
    public void buildURIWithoutEncoding() throws UnsupportedEncodingException, URISyntaxException {
        String queryParams = "param1=The+string+%C3%BC%40foo-bar";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("true").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param1=The+string+%C3%BC%40foo-bar", uri.toString());
    }

    @Test
    public void buildURIWithoutQueryParams() throws UnsupportedEncodingException, URISyntaxException {
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("true").setQueryParams(null).buildURI();
        assertEquals(URL, uri.toString());
    }

    @Test
    public void buildURIWithException() throws UnsupportedEncodingException, URISyntaxException {
        String url = "http://[localhost]:8002";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("could not parse");
        new URIBuilder().setUrl(url).setQueryParamsAreURLEncoded("true").setQueryParams("").buildURI();
    }
}
