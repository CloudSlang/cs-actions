package io.cloudslang.content.httpclient.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.utils.Constants.HTTP;
import static io.cloudslang.content.httpclient.utils.Constants.HTTPS;
import static io.cloudslang.content.httpclient.utils.Constants.POINTS;

public class CustomConnectionManager {

    private static GlobalSessionObject<Map<String, PoolingHttpClientConnectionManager>> connectionPoolHolder;
    private static String connectionManagerMapKey;

    public CustomConnectionManager setConnectionPoolHolder(GlobalSessionObject connectionPoolHolder) {
        this.connectionPoolHolder = connectionPoolHolder;
        return this;
    }

    public CustomConnectionManager setConnectionManagerMapKey(String... connectionManagerMapKeys) {
        this.connectionManagerMapKey = buildConnectionManagerMapKey(connectionManagerMapKeys);
        return this;
    }

    public static String buildConnectionManagerMapKey(String... connectionManagerMapKeys) {
        StringBuilder keyBuilder = new StringBuilder();
        for (String token : connectionManagerMapKeys) {
            keyBuilder.append(token).append(POINTS);
        }
        if (keyBuilder.length() > 0) {
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        return keyBuilder.toString();
    }

    public static PoolingHttpClientConnectionManager getConnectionManager(HttpClientInputs httpClientInputs,
                                                                     SSLConnectionSocketFactory sslConnectionSocketFactory,
                                                                     URI uri) {
        if (connectionPoolHolder != null) {
            PoolingHttpClientConnectionManager connManager = null;
            synchronized (connectionPoolHolder) {
                Map<String, PoolingHttpClientConnectionManager> connectionManagerMap = connectionPoolHolder.get();

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
                            .register(HTTP, PlainConnectionSocketFactory.INSTANCE)
                            .register(HTTPS, sslConnectionSocketFactory)
                            .build();

                    connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                    connectionManagerMap.put(connectionManagerMapKey, connManager);
                }
            }

            SocketConfig socketConfig = SocketConfig.DEFAULT;
            connManager.setDefaultSocketConfig(socketConfig);

            connManager.setMaxTotal(Integer.parseInt(httpClientInputs.getConnectionsMaxTotal()));
            connManager.setDefaultMaxPerRoute(Integer.parseInt(httpClientInputs.getConnectionsMaxPerRoute()));
            return connManager;
        }
        return null;
    }

}

