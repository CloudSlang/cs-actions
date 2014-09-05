package com.score.content.httpclient.build;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.*;
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
        new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).build();
    }

    @Test
    public void testMethods() throws URISyntaxException {
        HttpRequestBase httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("GET").build();
        assertEquals(httpRequestBase.getMethod(), "GET");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("POST").build();
        assertEquals(httpRequestBase.getMethod(), "POST");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("PUT").build();
        assertEquals(httpRequestBase.getMethod(), "PUT");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("DELETE").build();
        assertEquals(httpRequestBase.getMethod(), "DELETE");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("TRACE").build();
        assertEquals(httpRequestBase.getMethod(), "TRACE");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("OPTIONS").build();
        assertEquals(httpRequestBase.getMethod(), "OPTIONS");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("HEAD").build();
        assertEquals(httpRequestBase.getMethod(), "HEAD");

        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("get").build();
        assertEquals(httpRequestBase.getMethod(), "GET");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("post").build();
        assertEquals(httpRequestBase.getMethod(), "POST");
        httpRequestBase = new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("put").build();
        assertEquals(httpRequestBase.getMethod(), "PUT");
    }

    @Test
    public void testEntity() throws URISyntaxException, IOException {
        HttpEntityEnclosingRequestBase httpRequestBase = (HttpEntityEnclosingRequestBase)new com.score.content.httpclient.build.RequestBuilder().setUri(new URI("https://localhost:443/lalal?dd=3")).setMethod("POST").setEntity(
                new StringEntity("my custom entity")).build();
        assertEquals(IOUtils.toString(httpRequestBase.getEntity().getContent()), "my custom entity");
    }
}
