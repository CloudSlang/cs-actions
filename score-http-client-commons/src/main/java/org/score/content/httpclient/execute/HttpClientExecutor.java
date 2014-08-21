package org.score.content.httpclient.execute;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

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
        try {
            return closeableHttpClient.execute(httpRequestBase, context);
        } catch (IOException e) {
            throw new IllegalArgumentException("error while executing http request: " + e.getMessage(), e);
        }
    }
}
