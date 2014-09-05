package org.score.content.httpclient.build;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionResource;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
                .setConnectionPoolHolder(new GlobalSessionObject())
                .buildConnectionManager();
        assertNotNull(connectionManager);
    }

    @Test
    public void buildConnectionManagerNewConnectionManager1() {
        GlobalSessionObject holder = new GlobalSessionObject();
        final Map<String, PoolingHttpClientConnectionManager> connectionManagerMap = new HashMap();
        connectionManagerMap.put("key1:key2", connectionManagerMock);
        holder.setResource(new SessionResource() {
            @Override
            public Object get() {
                return connectionManagerMap;
            }
            @Override
            public void release() {
            }
        });
        PoolingHttpClientConnectionManager connectionManager = new ConnectionManagerBuilder()
                .setConnectionManagerMapKey("key1", "key2")
                .setSslsf(sslConnectionSocketFactoryMock)
                .setConnectionPoolHolder(holder)
                .buildConnectionManager();
        assertEquals(connectionManagerMock, connectionManager);
    }
}
