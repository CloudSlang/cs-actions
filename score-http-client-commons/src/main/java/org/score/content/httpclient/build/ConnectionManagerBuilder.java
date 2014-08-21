package org.score.content.httpclient.build;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.score.content.httpclient.SessionObjectHolder;

import java.util.HashMap;
import java.util.Map;

public class ConnectionManagerBuilder {
    private SessionObjectHolder connectionPoolHolder;
    private SSLConnectionSocketFactory sslsf;
    private String connectionManagerMapKey;

    public ConnectionManagerBuilder setConnectionPoolHolder(SessionObjectHolder connectionPoolHolder) {
        this.connectionPoolHolder = connectionPoolHolder;
        return this;
    }

    public ConnectionManagerBuilder setSslsf(SSLConnectionSocketFactory sslsf) {
        this.sslsf = sslsf;
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
                //the DefaultMaxPerRoute default is 2
                //connManager.setDefaultMaxPerRoute(1);
                connectionManagerMap.put(connectionManagerMapKey, connManager);
            }
            return connManager;
        }
        return null;
    }
}