package org.score.content.httpclient.build;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.score.content.httpclient.SessionObjectHolder;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionManagerBuilderTest {

    @Mock
    private SSLConnectionSocketFactory sslConnectionSocketFactoryMock;
    @Mock
    private PoolingHttpClientConnectionManager connectionManagerMock;

    @Test
    public void buildConnectionManagerWithNullPoolHolder() {
        PoolingHttpClientConnectionManager connectionManager = new ConnectionManagerBuilder()
                .setConnectionManagerMapKey("key1", "key2")
                .setConnectionPoolHolder(null)
                .buildConnectionManager();
        assertNull(connectionManager);
    }

    @Test
    public void buildConnectionManagerNewConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new ConnectionManagerBuilder()
                .setConnectionManagerMapKey("key1", "key2")
                .setSslsf(sslConnectionSocketFactoryMock)
                .setConnectionPoolHolder(new SessionObjectHolder())
                .buildConnectionManager();
        assertNotNull(connectionManager);
    }

    @Test
    public void buildConnectionManagerNewConnectionManager1() {
        SessionObjectHolder holder = new SessionObjectHolder();
        Map<String, PoolingHttpClientConnectionManager> connectionManagerMap = new HashMap<String, PoolingHttpClientConnectionManager>();
        connectionManagerMap.put("key1:key2", connectionManagerMock);
        holder.setObject(connectionManagerMap);
        PoolingHttpClientConnectionManager connectionManager = new ConnectionManagerBuilder()
                .setConnectionManagerMapKey("key1", "key2")
                .setSslsf(sslConnectionSocketFactoryMock)
                .setConnectionPoolHolder(holder)
                .buildConnectionManager();
        assertEquals(connectionManagerMock, connectionManager);
    }
}
