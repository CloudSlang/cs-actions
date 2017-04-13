/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.execute;

import io.cloudslang.content.couchbase.entities.inputs.BucketInputs;
import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getHttpClientInputs;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by TusaM
 * 4/11/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({CSHttpClient.class, CouchbaseService.class})
public class CouchbaseServiceTest {
    @Mock
    private CSHttpClient csHttpClientMock;

    private CouchbaseService toTest;
    private HttpClientInputs httpClientInputs;

    @Before
    public void init() throws Exception {
        whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(new HashMap<String, String>());
        toTest = new CouchbaseService();
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
    }

    @Test
    public void testGetAllBuckets() throws MalformedURLException {
        CommonInputs commonInputs = getCommonInputs("GetAllBuckets", "buckets", "http://somewhere.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets", httpClientInputs.getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
    }

    @Test
    public void testGetBucketStatistics() throws MalformedURLException {
        CommonInputs commonInputs = getCommonInputs("GetBucketStatistics", "buckets", "http://somewhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("testBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets/testBucket/stats", httpClientInputs.getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
    }

    @Test
    public void testGetBucket() throws MalformedURLException {
        CommonInputs commonInputs = getCommonInputs("GetBucket", "buckets", "http://somewhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("specifiedBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets/specifiedBucket", httpClientInputs.getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
    }

    @Test
    public void testDeleteBucket() throws MalformedURLException {
        CommonInputs commonInputs = getCommonInputs("DeleteBucket", "buckets", "http://somewhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("toBeDeletedBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets/toBeDeletedBucket", httpClientInputs.getUrl());
        assertEquals("application/json", httpClientInputs.getContentType());
    }

    private CommonInputs getCommonInputs(String action, String api, String endpoint) {
        return new CommonInputs.Builder()
                    .withAction(action)
                    .withApi(api)
                    .withEndpoint(endpoint)
                    .build();
    }
}