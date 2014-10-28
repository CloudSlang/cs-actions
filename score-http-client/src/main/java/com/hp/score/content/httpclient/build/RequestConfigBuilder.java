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

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import com.hp.score.content.httpclient.HttpClientInputs;

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
                throw new IllegalArgumentException("Cound not parse '"+ HttpClientInputs.PROXY_PORT
                        +"' input: " + e.getMessage(), e);
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