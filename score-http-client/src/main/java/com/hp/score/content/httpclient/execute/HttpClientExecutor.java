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
package com.hp.score.content.httpclient.execute;

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
