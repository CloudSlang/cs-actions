/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient;


import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

/**
 * User: bancl
 * Date: 10/16/2015
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSHttpClient.class})
public class CSHttpClientTest {

    private CSHttpClient csHttpClient;
    @Mock
    private HttpClientInputs httpClientInputs;
    @Mock
    private HttpRequestBase httpRequestBase;
    @Mock
    private HttpComponents httpComponents;
    @Mock
    private CloseableHttpClient closeableHttpClient;
    @Mock
    private HttpClientContext httpClientContext;
    @Mock
    private CloseableHttpResponse httpResponse;
    @Mock
    private String responseCharacterSet;
    @Mock
    private String destinationFile;
    @Mock
    private URI uri;
    @Mock
    private Map<String, String> result;
    @Mock
    private CookieStore cookieStore;
    @Mock
    private SerializableSessionObject serializableSessionObject;
    @Mock
    private PoolingHttpClientConnectionManager connManager;

    @Before
    public void setUp() throws Exception {
        csHttpClient = PowerMockito.spy(new CSHttpClient());

        PowerMockito.doNothing().when(csHttpClient, "initSessionsObjects", httpClientInputs);
        PowerMockito.doReturn(httpComponents).when(csHttpClient, "buildHttpComponents", httpClientInputs);
        PowerMockito.doReturn(httpResponse).when(csHttpClient, "execute", closeableHttpClient, httpRequestBase, httpClientContext);
        PowerMockito.doReturn(result).when(csHttpClient, "parseResponse", httpResponse, responseCharacterSet, destinationFile,
                uri, httpClientContext, cookieStore, serializableSessionObject);

        PowerMockito.when(httpComponents.getHttpRequestBase()).thenReturn(httpRequestBase);
        PowerMockito.when(httpComponents.getCloseableHttpClient()).thenReturn(closeableHttpClient);
        PowerMockito.when(httpComponents.getHttpClientContext()).thenReturn(httpClientContext);
        PowerMockito.when(httpComponents.getUri()).thenReturn(uri);
        PowerMockito.when(httpComponents.getCookieStore()).thenReturn(cookieStore);
        PowerMockito.when(httpComponents.getConnManager()).thenReturn(connManager);

        PowerMockito.when(httpClientInputs.getResponseCharacterSet()).thenReturn(responseCharacterSet);
        PowerMockito.when(httpClientInputs.getDestinationFile()).thenReturn(destinationFile);
        PowerMockito.when(httpClientInputs.getCookieStoreSessionObject()).thenReturn(serializableSessionObject);
    }

    @Test
    public void executeKeepAliveTrue() {
        PowerMockito.when(httpClientInputs.getKeepAlive()).thenReturn("true");
        Map<String, String> result1 = csHttpClient.execute(httpClientInputs);
        assertEquals(result, result1);
        Mockito.verify(httpRequestBase, times(1)).releaseConnection();
    }

    @Test
    public void executeKeepAliveFalse() throws IOException {
        PowerMockito.when(httpClientInputs.getKeepAlive()).thenReturn("false");
        Map<String, String> result1 = csHttpClient.execute(httpClientInputs);
        assertEquals(result, result1);
        Mockito.verify(httpResponse, times(1)).close();
        Mockito.verify(httpRequestBase, times(1)).releaseConnection();
        Mockito.verify(connManager, times(1)).closeExpiredConnections();
    }
}
