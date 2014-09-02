package org.score.content.httpclient.build;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.score.content.httpclient.HttpClientInputs;
import org.score.content.httpclient.SessionObjectHolder;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManagerBuilder {
    private SessionObjectHolder connectionPoolHolder;
    private SSLConnectionSocketFactory sslsf;
    private String connectionManagerMapKey;
    private String defaultMaxPerRoute;
    private String totalMax;

    public ConnectionManagerBuilder setConnectionPoolHolder(SessionObjectHolder connectionPoolHolder) {
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
        StringBuilder keyBuilder = new StringBuilder();
        for (String token : connectionManagerMapKeys) {
            keyBuilder.append(token).append(":");
        }
        if (keyBuilder.length() > 0) {
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        this.connectionManagerMapKey = keyBuilder.toString();
        return this;
    }

    public synchronized PoolingHttpClientConnectionManager buildConnectionManager() {
        if (connectionPoolHolder != null) {
            @SuppressWarnings("unchecked") Map<String, PoolingHttpClientConnectionManager> connectionManagerMap
                    = (Map<String, PoolingHttpClientConnectionManager>) connectionPoolHolder.getObject();

            if (connectionManagerMap == null) {
                connectionManagerMap = new HashMap<>();
                //noinspection unchecked
                connectionPoolHolder.setObject(connectionManagerMap);
            }

            PoolingHttpClientConnectionManager connManager = connectionManagerMap.get(connectionManagerMapKey);
            if (connManager == null) {
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", sslsf)
                        .build();
                connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

                connectionManagerMap.put(connectionManagerMapKey, connManager);
            }
            //the DefaultMaxPerRoute default is 2
            if (!StringUtils.isEmpty(defaultMaxPerRoute)) {
                try {
                    connManager.setDefaultMaxPerRoute(Integer.parseInt(defaultMaxPerRoute));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("the '"+ HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE
                            +"' input should be integer" +e.getMessage(), e);
                }
            }
            //the Default totalMax default is 20
            if (!StringUtils.isEmpty(totalMax)) {
                try {
                    connManager.setMaxTotal(Integer.parseInt(totalMax));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("the '"+ HttpClientInputs.CONNECTIONS_MAX_TOTAL
                            +"' input should be integer" +e.getMessage(), e);
                }
            }
            return connManager;
        }
        return null;
    }
}