package org.score.content.httpclient;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

public class ProxyRouteBuilder {
    private String proxyHost;
    private String proxyPort = "8080";

    public ProxyRouteBuilder setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public ProxyRouteBuilder setProxyPort(String proxyPort) {
        if (!StringUtils.isEmpty(proxyPort)) {
            this.proxyPort = proxyPort;
        }
        return this;
    }

    public HttpRoutePlanner buildProxyRoute() {
        if (proxyHost != null) {
            HttpHost proxyHttpHost = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            return new DefaultProxyRoutePlanner(proxyHttpHost);
        }
        return null;
    }
}