/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.execute;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/28/14
 */
public class HttpClientExecutor {
    private HttpRequestBase httpRequestBase;
    private CloseableHttpClient closeableHttpClient;
    private HttpClientContext context;

    public HttpClientExecutor setHttpRequestBase(HttpRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
        return this;
    }

    public HttpClientExecutor setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
        return this;
    }

    public HttpClientExecutor setContext(HttpClientContext context) {
        this.context = context;
        return this;
    }

    public CloseableHttpResponse execute() {
        CloseableHttpResponse response;
        try {
            response = closeableHttpClient.execute(httpRequestBase, context);
        } catch (SocketTimeoutException ste) {
            throw new RuntimeException("Socket timeout: " + ste.getMessage(), ste);
        } catch (HttpHostConnectException connectEx) {
            throw new RuntimeException("Connection error: " + connectEx.getMessage(), connectEx);
        } catch (IOException e) {
            throw new RuntimeException("Error while executing http request: " + e.getMessage(), e);
        }
        return response;
    }
}
