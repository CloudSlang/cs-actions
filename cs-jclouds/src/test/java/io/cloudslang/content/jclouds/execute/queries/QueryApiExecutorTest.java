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
    private static final String HEADERS = "Accept:text/plain\r\n" +
            "Content-Type:application/json";

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
    public void init() {
        toTest = new QueryApiExecutor();
    }

    @After
    public void tearDown() {
        toTest = null;
        queryApiExecutorSpy = null;
    }

    @Test
    public void testExecute() throws Exception {
        whenNew(AmazonSignatureService.class).withNoArguments().thenReturn(amazonSignatureServiceMock);
        when(amazonSignatureServiceMock.signRequestHeaders(any(AwsInputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class))).thenReturn(authorizationHeaderMock);
        doNothing().when(queryApiExecutorSpy).setQueryApiCallHeaders(any(AwsInputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(null);

        toTest.execute(getCommonInputs(HEADERS, ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs(), "CreateVolume");

        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(AwsInputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap()));
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));

        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    @Test
    public void testExecuteExceptionTest() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Unsupported Query API.");

        toTest.execute(getCommonInputs("", ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs(), "");

        verify(amazonSignatureServiceMock, never()).signRequestHeaders(any(AwsInputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        verify(csHttpClientMock, never()).execute(any(HttpClientInputs.class));
    }

    private CommonInputs getCommonInputs(String headersString, String queryParamsString) throws MalformedURLException {
        return new CommonInputs.CommonInputsBuilder()
                .withHeaders(headersString)
                .withQueryParams(queryParamsString)
                .withVersion("2016-04-01")
                .build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder().withVolumeType("").withAvailabilityZone("us-east-1d").build();
    }

    private VolumeInputs getVolumeInputs() {
        return new VolumeInputs.VolumeInputsBuilder().withSize("10").withIops("").build();
    }

    private NetworkInputs getNetworkInputs() {
        return new NetworkInputs.NetworkInputsBuilder().build();
    }

    private Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("accept", "text/plain");
        headersMap.put("content-type", "application/json");

        return headersMap;
    }

    private Map<String, String> getQueryParamsMap() {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put("Action", "CreateVolume");
        queryParamsMap.put("VolumeType", "standard");
        queryParamsMap.put("Version", "2016-04-01");
        queryParamsMap.put("Size", "10");
        queryParamsMap.put("AvailabilityZone", "us-east-1d");

        return queryParamsMap;
    }
}