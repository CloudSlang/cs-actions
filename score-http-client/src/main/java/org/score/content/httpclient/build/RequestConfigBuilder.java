package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;

public class RequestConfigBuilder {
    private String connectionTimeout = "-1";
    private String socketTimeout = "-1";
    private String followRedirects = "true";
    private String proxyHost;
    private String proxyPort = "8080";

    public RequestConfigBuilder setConnectionTimeout(String connectionTimeout) {
        if (!StringUtils.isEmpty(connectionTimeout)) {
            this.connectionTimeout = connectionTimeout;
        }
        return this;
    }

    public RequestConfigBuilder setSocketTimeout(String socketTimeout) {
        if (!StringUtils.isEmpty(socketTimeout)) {
            this.socketTimeout = socketTimeout;
        }
        return this;
    }

    public RequestConfigBuilder setFollowRedirects(String followRedirects) {
        if (!StringUtils.isEmpty(followRedirects)) {
            this.followRedirects = followRedirects;
        }
        return this;
    }

    public RequestConfigBuilder setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public RequestConfigBuilder setProxyPort(String proxyPort) {
        if (!StringUtils.isEmpty(proxyPort)) {
            this.proxyPort = proxyPort;
        }
        return this;
    }

    public RequestConfig buildRequestConfig() {
        HttpHost proxy = null;
        if (proxyHost != null && !proxyHost.isEmpty()) {
            proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
        }
        return RequestConfig.custom()
                .setConnectTimeout(Integer.parseInt(connectionTimeout))
                        //todo not set for post ?
                .setSocketTimeout(Integer.parseInt(socketTimeout))
                .setProxy(proxy)
                .setRedirectsEnabled(Boolean.parseBoolean(followRedirects)).build();
    }
}