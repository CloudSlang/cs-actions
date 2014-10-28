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

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import com.hp.score.content.httpclient.HttpClientInputs;

import java.net.URI;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 8/28/14
 */
public class RequestBuilder {
    //ordered
    private static String [] methods = new String []{HttpDelete.METHOD_NAME, HttpGet.METHOD_NAME, HttpHead.METHOD_NAME,
            HttpOptions.METHOD_NAME, HttpPatch.METHOD_NAME, HttpPost.METHOD_NAME, HttpPut.METHOD_NAME,  HttpTrace.METHOD_NAME};

    private String method;
    private URI uri;
    private HttpEntity entity;

    public RequestBuilder setMethod(String method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public RequestBuilder setEntity(HttpEntity entity) {
        this.entity = entity;
        return this;
    }

    public HttpRequestBase build() {
        if (method==null) {
            throw new IllegalArgumentException("The 'method' input is required. Provide one of " + Arrays.asList(methods).toString());
        }
        String method = this.method.toUpperCase().trim();
        if (Arrays.binarySearch(methods, method) < 0) {
            throw new IllegalArgumentException("invalid '"+ HttpClientInputs.METHOD+"' input '" + method + "'");
        }

        org.apache.http.client.methods.RequestBuilder requestBuilder
                = org.apache.http.client.methods.RequestBuilder.create(method);
        requestBuilder.setUri(uri);
        requestBuilder.setEntity(entity);

        return (HttpRequestBase) requestBuilder.build();
    }
}
