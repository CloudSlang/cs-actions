/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.openscore.content.httpclient.HttpClientInputs;

public class RequestConfigBuilder {
    private String connectionTimeout = "0";
    private String socketTimeout = "0";
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
            try {
                proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value '" + proxyPort + "' for input '" + HttpClientInputs.PROXY_PORT
                        + "'. Valid Values: Integer values greater then 0. " , e);
            }
        }
        int connectionTimeout = Integer.parseInt(this.connectionTimeout);
        int socketTimeout = Integer.parseInt(this.socketTimeout);
        //todo should we also allow user to enable redirects prohibited by the HTTP specification (on POST and PUT)? See 'LaxRedirectStrategy'
        return RequestConfig.custom()
                .setConnectTimeout(connectionTimeout <= 0 ? connectionTimeout : connectionTimeout * 1000)
                .setSocketTimeout(socketTimeout <= 0 ? socketTimeout : socketTimeout * 1000)
                .setProxy(proxy)
                .setRedirectsEnabled(Boolean.parseBoolean(followRedirects)).build();
    }
}