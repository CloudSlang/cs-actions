package io.cloudslang.content.jclouds.execute.queries;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.inputs.*;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.services.impl.MockingHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by Mihai Tusa.
 * 9/7/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CSHttpClient.class, AmazonSignatureService.class, QueryApiExecutor.class})
public class QueryApiExecutorTest {
    private static final String HEADERS = "Accept:text/plain\r\n Content-Type:application/json";

    private static QueryApiExecutor toTest;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private CSHttpClient csHttpClientMock;

    @Mock
    private AmazonSignatureService amazonSignatureServiceMock;

    @Mock
    private AuthorizationHeader authorizationHeaderMock;

    @Spy
    private QueryApiExecutor queryApiExecutorSpy = new QueryApiExecutor();

    @Before
    public void init() throws Exception {
        toTest = new QueryApiExecutor();
        addCommonMocksForQueryApi();
    }

    @After
    public void tearDown() {
        toTest = null;
        queryApiExecutorSpy = null;
    }

    @Test
    public void testCreateVolume() throws Exception {
        toTest.execute(getCommonInputs("CreateVolume", HEADERS, ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs());

        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(AwsInputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateVolume")));
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));

        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    @Test
    public void testAllocateAddress() throws Exception {
        toTest.execute(getCommonInputs("AllocateAddress", HEADERS, ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs());

        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(AwsInputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AllocateAddress")));
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));

        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    @Test
    public void testAttachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("AttachNetworkInterface", HEADERS, ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs());

        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(AwsInputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AttachNetworkInterface")));
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));

        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    @Test
    public void testDetachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DetachNetworkInterface", HEADERS, ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs());

        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(AwsInputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DetachNetworkInterface")));
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));

        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    @Test
    public void testExecuteException() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Unsupported Query API.");

        toTest.execute(getCommonInputs("", "", ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs());

        verify(amazonSignatureServiceMock, never()).signRequestHeaders(any(AwsInputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        verify(csHttpClientMock, never()).execute(any(HttpClientInputs.class));
    }

    private void addCommonMocksForQueryApi() throws Exception {
        whenNew(AmazonSignatureService.class).withNoArguments().thenReturn(amazonSignatureServiceMock);
        when(amazonSignatureServiceMock.signRequestHeaders(any(AwsInputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class))).thenReturn(authorizationHeaderMock);
        doNothing().when(queryApiExecutorSpy).setQueryApiCallHeaders(any(AwsInputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(null);
    }

    private CommonInputs getCommonInputs(String action, String headersString, String queryParamsString) throws MalformedURLException {
        return new CommonInputs.CommonInputsBuilder()
                .withAction(action)
                .withHeaders(headersString)
                .withQueryParams(queryParamsString)
                .withVersion("2016-04-01")
                .build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder()
                .withAvailabilityZone("us-east-1d")
                .withInstanceId("i-abcdef12")
                .withAttachmentId("eni-attach-12345678")
                .withVolumeType("")
                .withDomain("").build();
    }

    private VolumeInputs getVolumeInputs() {
        return new VolumeInputs.VolumeInputsBuilder().withSize("10").withIops("").build();
    }

    private NetworkInputs getNetworkInputs() {
        return new NetworkInputs.NetworkInputsBuilder()
                .withNetworkInterfaceId("eni-12345678")
                .withDeviceIndex("25")
                .build();
    }

    private Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("accept", "text/plain");
        headersMap.put("content-type", "application/json");

        return headersMap;
    }

    private Map<String, String> getQueryParamsMap(String action) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put("Action", action);
        queryParamsMap.put("Version", "2016-04-01");

        if ("CreateVolume" .equals(action)) {
            queryParamsMap.put("VolumeType", "standard");
            queryParamsMap.put("Size", "10");
            queryParamsMap.put("AvailabilityZone", "us-east-1d");
        }

        if ("AllocateAddress" .equals(action)) {
            queryParamsMap.put("Domain", "standard");
        }

        if ("AttachNetworkInterface".equals(action)) {
            queryParamsMap.put("InstanceId", "i-abcdef12");
            queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
            queryParamsMap.put("DeviceIndex", "25");
        }

        if ("DetachNetworkInterface".equals(action)) {
            queryParamsMap.put("AttachmentId", "eni-attach-12345678");
        }

        return queryParamsMap;
    }
}