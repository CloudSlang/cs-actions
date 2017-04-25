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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getHttpClientInputs;
import static io.cloudslang.content.couchbase.utils.TestUtils.setExpectedExceptions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
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
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private CSHttpClient csHttpClientMock;

    private CouchbaseService toTest;
    private HttpClientInputs httpClientInputs;

    @Before
    public void init() throws Exception {
        whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(new HashMap<String, String>());
        toTest = new CouchbaseService();
    }

    @Test
    public void testCreateOrEditBucket() throws MalformedURLException {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        BucketInputs bucketInputs = new BucketInputs.Builder()
                .withBucketName("toBeCreated")
                .withAuthType("")
                .withBucketType("")
                .withConflictResolutionType("")
                .withProxyPort("")
                .withEvictionPolicy("")
                .withFlushEnabled("")
                .withParallelDBAndViewCompaction("")
                .withRamQuotaMB("")
                .withReplicaIndex("")
                .withReplicaNumber("")
                .withSaslPassword("")
                .withThreadsNumber("")
                .build();
        CommonInputs commonInputs = getCommonInputs("CreateOrEditBucket", "buckets", "http://subdomain.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://subdomain.couchbase.com:8091/pools/default/buckets", httpClientInputs.getUrl());
        assertEquals("Accept:application/json, text/plain, */*", httpClientInputs.getHeaders());
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", httpClientInputs.getContentType());
        assertTrue(httpClientInputs.getBody().contains("name=toBeCreated"));
        assertTrue(httpClientInputs.getBody().contains("authType=none"));
        assertTrue(httpClientInputs.getBody().contains("bucketType=membase"));
        assertTrue(httpClientInputs.getBody().contains("conflictResolutionType=seqno"));
        assertTrue(httpClientInputs.getBody().contains("proxyPort=11215"));
        assertTrue(httpClientInputs.getBody().contains("evictionPolicy=valueOnly"));
        assertTrue(httpClientInputs.getBody().contains("flushEnabled=0"));
        assertTrue(httpClientInputs.getBody().contains("parallelDBAndViewCompaction=false"));
        assertTrue(httpClientInputs.getBody().contains("ramQuotaMB=100"));
        assertTrue(httpClientInputs.getBody().contains("replicaNumber=1"));
        assertTrue(httpClientInputs.getBody().contains("threadsNumber=2"));
    }

    @Test
    public void testCreateOrEditBucketWithoutSaslPassword() throws MalformedURLException {
        setExpectedExceptions(RuntimeException.class, exception, "The combination of values supplied for inputs: " +
                "authType, proxyPort and/or saslPassword doesn't meet conditions for general purpose usage.");

        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        BucketInputs bucketInputs = new BucketInputs.Builder()
                .withBucketName("toBeCreated")
                .withAuthType("sasl")
                .withBucketType("")
                .withConflictResolutionType("")
                .withProxyPort("")
                .withEvictionPolicy("")
                .withFlushEnabled("")
                .withParallelDBAndViewCompaction("")
                .withRamQuotaMB("")
                .withReplicaIndex("")
                .withReplicaNumber("")
                .withSaslPassword("")
                .withThreadsNumber("")
                .build();
        CommonInputs commonInputs = getCommonInputs("CreateOrEditBucket", "buckets", "http://subdomain.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verify(csHttpClientMock, never()).execute(eq(httpClientInputs));
    }

    @Test
    public void testGetAllBuckets() throws MalformedURLException {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
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
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
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
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
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
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "DELETE");
        CommonInputs commonInputs = getCommonInputs("DeleteBucket", "buckets", "http://anywhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("toBeDeletedBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://anywhere.couchbase.com:8091/pools/default/buckets/toBeDeletedBucket", httpClientInputs.getUrl());
        assertEquals("application/json", httpClientInputs.getContentType());
    }

    @Test
    public void testGetClusterInfo() throws MalformedURLException {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetClusterInfo", "cluster", "http://whatever.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://whatever.couchbase.com:8091/pools", httpClientInputs.getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.getHeaders());
        assertEquals("application/json", httpClientInputs.getContentType());
    }

    @Test
    public void testGetClusterDetails() throws MalformedURLException {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetClusterDetails", "cluster", "http://whatever.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verify(csHttpClientMock, times(1)).execute(eq(httpClientInputs));
        verifyNoMoreInteractions(csHttpClientMock);

        assertEquals("http://whatever.couchbase.com:8091/pools/default", httpClientInputs.getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.getHeaders());
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