/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package io.cloudslang.content.couchbase.execute;

import io.cloudslang.content.couchbase.entities.inputs.BucketInputs;
import io.cloudslang.content.couchbase.entities.inputs.ClusterInputs;
import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.couchbase.entities.inputs.NodeInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getHttpClientInputs;
import static io.cloudslang.content.couchbase.utils.TestUtils.setExpectedExceptions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Created by TusaM
 * 4/11/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClientService.class, CouchbaseService.class})
public class CouchbaseServiceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private CouchbaseService toTest;
    private HttpClientInputs.HttpClientInputsBuilder httpClientInputs;

    @Before
    public void init() throws Exception {
        org.powermock.api.mockito.PowerMockito.mockStatic(HttpClientService.class);
        org.powermock.api.mockito.PowerMockito.when(HttpClientService.execute(any(HttpClientInputs.class)))
                .thenReturn(new HashMap<String, String>());
        toTest = new CouchbaseService();
    }

    @Test
    public void testCreateOrEditBucket() throws Exception {
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

        verifyHttpClientExecute(times(1));


        assertEquals("http://subdomain.couchbase.com:8091/pools/default/buckets", httpClientInputs.build().getUrl());
        assertEquals("Accept:application/json, text/plain, */*", httpClientInputs.build().getHeaders());
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("name=toBeCreated"));
        assertTrue(httpClientInputs.build().getBody().contains("authType=none"));
        assertTrue(httpClientInputs.build().getBody().contains("bucketType=membase"));
        assertTrue(httpClientInputs.build().getBody().contains("conflictResolutionType=seqno"));
        assertTrue(httpClientInputs.build().getBody().contains("proxyPort=11215"));
        assertTrue(httpClientInputs.build().getBody().contains("evictionPolicy=valueOnly"));
        assertTrue(httpClientInputs.build().getBody().contains("flushEnabled=0"));
        assertTrue(httpClientInputs.build().getBody().contains("parallelDBAndViewCompaction=false"));
        assertTrue(httpClientInputs.build().getBody().contains("ramQuotaMB=100"));
        assertTrue(httpClientInputs.build().getBody().contains("replicaNumber=1"));
        assertTrue(httpClientInputs.build().getBody().contains("threadsNumber=2"));
    }

    @Test
    public void testCreateOrEditBucketWithoutSaslPassword() throws Exception {
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

        verifyHttpClientExecute(never());
    }

    @Test
    public void testFlushBucket() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        CommonInputs commonInputs = getCommonInputs("FlushBucket", "buckets", "http://anywhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("toBeFlushedBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://anywhere.couchbase.com:8091/pools/default/buckets/toBeFlushedBucket/controller/doFlush", httpClientInputs.build().getUrl());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testDeleteBucket() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "DELETE");
        CommonInputs commonInputs = getCommonInputs("DeleteBucket", "buckets", "http://anywhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("toBeDeletedBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://anywhere.couchbase.com:8091/pools/default/buckets/toBeDeletedBucket", httpClientInputs.build().getUrl());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetAllBuckets() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetAllBuckets", "buckets", "http://somewhere.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets", httpClientInputs.build().getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetAutoFailOverSettings() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetAutoFailOverSettings", "cluster", "http://somewhere.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://somewhere.couchbase.com:8091/settings/autoFailover", httpClientInputs.build().getUrl());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetBucket() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetBucket", "buckets", "http://somewhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("specifiedBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets/specifiedBucket", httpClientInputs.build().getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetBucketStatistics() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetBucketStatistics", "buckets", "http://somewhere.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("testBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://somewhere.couchbase.com:8091/pools/default/buckets/testBucket/stats", httpClientInputs.build().getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetClusterDetails() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetClusterDetails", "cluster", "http://whatever.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/pools/default", httpClientInputs.build().getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetClusterInfo() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetClusterInfo", "cluster", "http://whatever.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/pools", httpClientInputs.build().getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGetDesignDocsInfo() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetDesignDocsInfo", "views", "http://whatever.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("toGetDesignDocsBucket").build();
        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/pools/default/buckets/toGetDesignDocsBucket/ddocs", httpClientInputs.build().getUrl());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    @Test
    public void testFailOverNodeSuccess() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        CommonInputs commonInputs = getCommonInputs("FailOverNode", "nodes", "http://whatever.couchbase.com:8091");
        NodeInputs nodeInputs = new NodeInputs.Builder().withInternalNodeIpAddress("ns_2@10.0.0.2").build();
        toTest.execute(httpClientInputs, commonInputs, nodeInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/controller/failOver", httpClientInputs.build().getUrl());
        assertEquals("Accept:application/json, text/plain, */*", httpClientInputs.build().getHeaders());
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", httpClientInputs.build().getContentType());
    }

    @Test
    public void testGracefulFailOverNodeSuccess() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        CommonInputs commonInputs = getCommonInputs("GracefulFailOverNode", "nodes", "http://whatever.couchbase.com:8091");
        NodeInputs nodeInputs = new NodeInputs.Builder().withInternalNodeIpAddress("ns_2@10.0.0.2").build();
        toTest.execute(httpClientInputs, commonInputs, nodeInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/controller/startGracefulFailover", httpClientInputs.build().getUrl());
        assertEquals("Accept:application/json, text/plain, */*", httpClientInputs.build().getHeaders());
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", httpClientInputs.build().getContentType());
    }

    @Test
    public void testRebalancingNodes() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        CommonInputs commonInputs = getCommonInputsWithDelimiter("RebalancingNodes", "cluster",
                "http://whatever.couchbase.com:8091", "");
        ClusterInputs clusterInputs = new ClusterInputs.Builder()
                .withEjectedNodes("ns_2@10.0.0.4,ns_2@10.0.0.5,ns_2@10.0.0.6")
                .withKnownNodes("ns_2@10.0.0.2,ns_2@10.0.0.3")
                .build();
        toTest.execute(httpClientInputs, commonInputs, clusterInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/controller/rebalance", httpClientInputs.build().getUrl());
        assertEquals("Accept:application/json, text/plain, */*", httpClientInputs.build().getHeaders());
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("ejectedNodes="));
        assertTrue(httpClientInputs.build().getBody().contains("ns_2@10.0.0.4"));
        assertTrue(httpClientInputs.build().getBody().contains("ns_2@10.0.0.5"));
        assertTrue(httpClientInputs.build().getBody().contains("ns_2@10.0.0.6"));
        assertTrue(httpClientInputs.build().getBody().contains("knownNodes="));
        assertTrue(httpClientInputs.build().getBody().contains("ns_2@10.0.0.2"));
        assertTrue(httpClientInputs.build().getBody().contains("ns_2@10.0.0.3"));
    }

    @Test
    public void testSetRecoveryTypes() throws Exception {
        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "POST");
        CommonInputs commonInputs = getCommonInputs("SetRecoveryType", "nodes", "http://whatever.couchbase.com:8091");
        NodeInputs nodeInputs = new NodeInputs.Builder().withInternalNodeIpAddress("ns_2@10.0.0.2").withRecoveryType("full").build();
        toTest.execute(httpClientInputs, commonInputs, nodeInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://whatever.couchbase.com:8091/controller/setRecoveryType", httpClientInputs.build().getUrl());
        assertEquals("Accept:application/json, text/plain, */*", httpClientInputs.build().getHeaders());
        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", httpClientInputs.build().getContentType());
        assertTrue(httpClientInputs.build().getBody().contains("otpNode=ns_2@10.0.0.2"));
        assertTrue(httpClientInputs.build().getBody().contains("recoveryType=full"));
    }

    @Test
    public void testFailOverNodeNoIPv4Address() throws Exception {
        setExpectedExceptions(RuntimeException.class, exception, "The value of: [ blah blah blah ] input as part " +
                "of: [ns_2@ blah blah blah ] input must be a valid IPv4 address.");

        CommonInputs commonInputs = getCommonInputs("FailOverNode", "nodes", "http://whatever.couchbase.com:8091");
        NodeInputs nodeInputs = new NodeInputs.Builder().withInternalNodeIpAddress("ns_2@ blah blah blah ").build();
        toTest.execute(httpClientInputs, commonInputs, nodeInputs);

        verifyHttpClientExecute(never());
    }

    @Test
    public void testFailOverNodeInvalidInternalNodeIpAddress() throws Exception {
        setExpectedExceptions(RuntimeException.class, exception, "The provided value for: " +
                "\" anything here but not [at] symbol \" input must be a valid Couchbase internal node format.");

        CommonInputs commonInputs = getCommonInputs("FailOverNode", "nodes", "http://whatever.couchbase.com:8091");
        NodeInputs nodeInputs = new NodeInputs.Builder().withInternalNodeIpAddress(" anything here but not [at] symbol ").build();
        toTest.execute(httpClientInputs, commonInputs, nodeInputs);

        verifyHttpClientExecute(never());
    }

    @Test
    public void testUnknownApi() throws Exception {
        setExpectedExceptions(RuntimeException.class, exception, "Unsupported Couchbase API.");

        httpClientInputs = getHttpClientInputs("someUser", "credentials", "", "",
                "", "", "", "", "", "",
                "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetDesignDocsInfo", "The Wizard of Oz", "http://whatever.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("anyBucket").build();

        toTest.execute(httpClientInputs, commonInputs, bucketInputs);

        verifyHttpClientExecute(never());
    }

    @Test
    public void testUnknownBuilderType() throws Exception {
        setExpectedExceptions(RuntimeException.class, exception, "Unknown builder type.");

        httpClientInputs = getHttpClientInputs("someUser", "credentials", "proxy.example.com", "8080",
                "some", "any", "", "strict", "C:\\temp\\keystore.jks", "changeit",
                "C:\\temp\\keystore.jks", "changeit", "15", "10", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetDesignDocsInfo", "views", "http://whatever.couchbase.com:8091");
        BucketInputs bucketInputs = new BucketInputs.Builder().withBucketName("anyBucket").build();

        toTest.execute(httpClientInputs, commonInputs, bucketInputs, null);

        verifyHttpClientExecute(never());
    }

    @Test
    public void testGetDestinationClusterReference() throws Exception {
        httpClientInputs = getHttpClientInputs("anonymous", "credentials", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "GET");
        CommonInputs commonInputs = getCommonInputs("GetDestinationClusterReference", "cluster", "http://somewhere.couchbase.com:8091");
        toTest.execute(httpClientInputs, commonInputs);

        verifyHttpClientExecute(times(1));


        assertEquals("http://somewhere.couchbase.com:8091/pools/default/remoteClusters", httpClientInputs.build().getUrl());
        assertEquals("X-memcachekv-Store-Client-Specification-Version:0.1", httpClientInputs.build().getHeaders());
        assertEquals("application/json", httpClientInputs.build().getContentType());
    }

    private CommonInputs getCommonInputs(String action, String api, String endpoint) {
        return new CommonInputs.Builder()
                .withAction(action)
                .withApi(api)
                .withEndpoint(endpoint)
                .build();
    }

    private void verifyHttpClientExecute(org.mockito.verification.VerificationMode mode) throws Exception {
        org.powermock.api.mockito.PowerMockito.verifyStatic(mode);
        HttpClientService.execute(any(HttpClientInputs.class));
    }

    private CommonInputs getCommonInputsWithDelimiter(String action, String api, String endpoint, String delimiter) {
        return new CommonInputs.Builder()
                .withAction(action)
                .withApi(api)
                .withEndpoint(endpoint)
                .withDelimiter(delimiter)
                .build();
    }
}