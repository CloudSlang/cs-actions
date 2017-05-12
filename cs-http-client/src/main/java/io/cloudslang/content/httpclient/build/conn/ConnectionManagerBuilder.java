/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.build.conn;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import io.cloudslang.content.httpclient.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManagerBuilder {
    private GlobalSessionObject<Map<String, PoolingHttpClientConnectionManager>> connectionPoolHolder;
    private SSLConnectionSocketFactory sslsf;
    private String connectionManagerMapKey;
    private String defaultMaxPerRoute;
    private String totalMax;

    public ConnectionManagerBuilder setConnectionPoolHolder(GlobalSessionObject connectionPoolHolder) {
        this.connectionPoolHolder = connectionPoolHolder;
        return this;
    }

    public ConnectionManagerBuilder setSslsf(SSLConnectionSocketFactory sslsf) {
        this.sslsf = sslsf;
        return this;
    }

    public ConnectionManagerBuilder setTotalMax(String totalMax) {
        this.totalMax = totalMax;
        return this;
    }

    public ConnectionManagerBuilder setDefaultMaxPerRoute(String defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
        return this;
    }

    public ConnectionManagerBuilder setConnectionManagerMapKey(String... connectionManagerMapKeys) {
        this.connectionManagerMapKey = buildConnectionManagerMapKey(connectionManagerMapKeys);
        return this;
    }

    public static String buildConnectionManagerMapKey(String... connectionManagerMapKeys) {
        StringBuilder keyBuilder = new StringBuilder();
        for (String token : connectionManagerMapKeys) {
            keyBuilder.append(token).append(":");
        }
        if (keyBuilder.length() > 0) {
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        return keyBuilder.toString();
    }

    public PoolingHttpClientConnectionManager buildConnectionManager() {
        if (connectionPoolHolder != null) {
            PoolingHttpClientConnectionManager connManager = null;
            synchronized (connectionPoolHolder) {
                Map<String, PoolingHttpClientConnectionManager> connectionManagerMap
                        = connectionPoolHolder.get();

                if (connectionManagerMap == null) {
                    final HashMap<String, PoolingHttpClientConnectionManager> connectionManagerMapFinal = new HashMap<>();
                    connectionPoolHolder.setResource(new SessionResource<Map<String, PoolingHttpClientConnectionManager>>() {
                        @Override
                        public Map<String, PoolingHttpClientConnectionManager> get() {
                            return connectionManagerMapFinal;
                        }

                        @Override
                        public void release() {
                        }
                    });
                    connectionManagerMap = connectionPoolHolder.get();
                }

                connManager = connectionManagerMap.get(connectionManagerMapKey);
                if (connManager == null) {
                    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslsf)
                            .build();
                    connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

                    connectionManagerMap.put(connectionManagerMapKey, connManager);
                }
            }

            //the DefaultMaxPerRoute default is 2
            if (!StringUtils.isEmpty(defaultMaxPerRoute)) {
                try {
                    connManager.setDefaultMaxPerRoute(Integer.parseInt(defaultMaxPerRoute));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("the '" + HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE
                            + "' input should be integer" + e.getMessage(), e);
                }
            }
            //the Default totalMax default is 20
            if (!StringUtils.isEmpty(totalMax)) {
                try {
                    connManager.setMaxTotal(Integer.parseInt(totalMax));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("the '" + HttpClientInputs.CONNECTIONS_MAX_TOTAL
                            + "' input should be integer" + e.getMessage(), e);
                }
            }
            return connManager;
        }
        return null;
    }
}