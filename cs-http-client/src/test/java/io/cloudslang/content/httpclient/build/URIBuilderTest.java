/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.build;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
        exception.expectMessage("the value 'http://[localhost]:8002' is not a valid URL");
        new URIBuilder().setUrl(url).setQueryParamsAreURLEncoded("true").setQueryParams("").buildURI();
    }

    @Test
    public void buildURIWithEncodingNotQueryParamsAsFormFalse() throws URISyntaxException {
        String queryParams = "param 1=value1&param 2=value2";
        URI uri = new URIBuilder().setUrl(URL).setQueryParams(queryParams).setQueryParamsAreFormEncoded("false").buildURI();
        assertEquals(URL + "?param%201=value1&param%202=value2", uri.toString());
    }

    @Test
    public void buildURIWithEncodingQueryParamsAsFormTrue() throws URISyntaxException {
        String queryParams = "param 1=value1&param 2=value2";
        URI uri = new URIBuilder().setUrl(URL).setQueryParams(queryParams).setQueryParamsAreFormEncoded("true").buildURI();
        assertEquals(URL + "?param+1=value1&param+2=value2", uri.toString());
    }

    @Test
    public void buildURIWithEncodingURLEncodedFalseFormTrue() throws URISyntaxException {
        String queryParams = "param 1=&param 2=\"";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("false").setQueryParamsAreFormEncoded("true").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param+1=&param+2=%22", uri.toString());
    }

    @Test
    public void buildURIWithEncodingURLEncodedFalseFormFalse() throws URISyntaxException {
        String queryParams = "param 1=\"&param 2=\"";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("false").setQueryParamsAreFormEncoded("false").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param%201=%22&param%202=%22", uri.toString());
    }

    @Test
    public void buildURIWithEncodingUrlTrueFormTrue() throws UnsupportedEncodingException, URISyntaxException {
        String queryParams = "param1=T%20he+string+%C3%BC%40foo-b a@r";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("true").setQueryParamsAreFormEncoded("true").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param1=T+he+string+%C3%BC%40foo-b+a%40r", uri.toString());
    }

    @Test
    public void buildURIWithEncodingUrlTrueFormFalse() throws UnsupportedEncodingException, URISyntaxException {
        String queryParams = ";/?:@&=+,$param1=T%20%2Bhe+string+%C3%BC%40foo-b a@r";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("true").setQueryParamsAreFormEncoded("false").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?;/?:@&=%20,$param1=T%20+he%20string%20%C3%BC@foo-b%20a@r", uri.toString());
    }

    @Test
    public void buildURIWithoutEncodingUrlFalseFormFalse() throws UnsupportedEncodingException, URISyntaxException {
        String queryParams = ";/?:@&=+,$param1=T%20he+string+%C3%BC%40foo-b a@r";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("false").setQueryParamsAreFormEncoded("false").setQueryParams(queryParams).buildURI();
        // if form encoded is false, then +  will remain as it is. The same for @
        assertEquals(URL + "?;/?:@&=+,$param1=T%2520he+string+%25C3%25BC%2540foo-b%20a@r", uri.toString());
    }

    @Test
    public void buildURIWithoutEncodingUrlFalseFormTrue() throws UnsupportedEncodingException, URISyntaxException {
        String queryParams = "param1=T%20he+string+%C3%BC%40foo-b a@r";
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("false").setQueryParamsAreFormEncoded("true").setQueryParams(queryParams).buildURI();
        assertEquals(URL + "?param1=T%2520he%2Bstring%2B%25C3%25BC%2540foo-b+a%40r", uri.toString());
    }

    @Test
    public void buildURIWithoutQueryParamsFormFalse() throws UnsupportedEncodingException, URISyntaxException {
        URI uri = new URIBuilder().setUrl(URL).setQueryParamsAreURLEncoded("true").setQueryParamsAreFormEncoded("false").setQueryParams(null).buildURI();
        assertEquals(URL, uri.toString());
    }

    @Test
    public void buildURIWithExceptionFormFalse() throws UnsupportedEncodingException, URISyntaxException {
        String url = "http://[localhost]:8002";
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("the value 'http://[localhost]:8002' is not a valid URL");
        new URIBuilder().setUrl(url).setQueryParamsAreURLEncoded("true").setQueryParams("").setQueryParamsAreFormEncoded("false").buildURI();
    }
}
