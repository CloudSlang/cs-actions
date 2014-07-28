package org.score.content.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;

public class RequestConfigBuilder {
    private String connectionTimeout = "-1";
    private String socketTimeout = "-1";
    private String followRedirects = "true";

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

    public RequestConfig buildRequestConfig() {

        return RequestConfig.custom()
                .setConnectTimeout(Integer.parseInt(connectionTimeout))
                        //todo not set for post ?
                .setSocketTimeout(Integer.parseInt(socketTimeout))
                .setRedirectsEnabled(Boolean.parseBoolean(followRedirects)).build();
    }
}