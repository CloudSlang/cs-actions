/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.content.httpclient.build;

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
        new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).build();
    }

    @Test
    public void testMethods() throws URISyntaxException {
        HttpRequestBase httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("GET").build();
        assertEquals(httpRequestBase.getMethod(), "GET");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("POST").build();
        assertEquals(httpRequestBase.getMethod(), "POST");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("PUT").build();
        assertEquals(httpRequestBase.getMethod(), "PUT");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("DELETE").build();
        assertEquals(httpRequestBase.getMethod(), "DELETE");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("TRACE").build();
        assertEquals(httpRequestBase.getMethod(), "TRACE");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("OPTIONS").build();
        assertEquals(httpRequestBase.getMethod(), "OPTIONS");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("HEAD").build();
        assertEquals(httpRequestBase.getMethod(), "HEAD");

        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("get").build();
        assertEquals(httpRequestBase.getMethod(), "GET");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("post").build();
        assertEquals(httpRequestBase.getMethod(), "POST");
        httpRequestBase = new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("/")).setMethod("put").build();
        assertEquals(httpRequestBase.getMethod(), "PUT");
    }

    @Test
    public void testEntity() throws URISyntaxException, IOException {
        HttpEntityEnclosingRequestBase httpRequestBase = (HttpEntityEnclosingRequestBase)new com.hp.score.content.httpclient.build.RequestBuilder().setUri(new URI("https://localhost:443/lalal?dd=3")).setMethod("POST").setEntity(
                new StringEntity("my custom entity")).build();
        assertEquals(IOUtils.toString(httpRequestBase.getEntity().getContent()), "my custom entity");
    }
}
