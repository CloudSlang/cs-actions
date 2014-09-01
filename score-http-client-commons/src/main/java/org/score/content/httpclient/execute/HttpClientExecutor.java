package org.score.content.httpclient.execute;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

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
