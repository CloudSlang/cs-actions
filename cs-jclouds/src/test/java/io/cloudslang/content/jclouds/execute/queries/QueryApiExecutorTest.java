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

    private QueryApiExecutor toTest;

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

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateVolume")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAllocateAddress() throws Exception {
        toTest.execute(getCommonInputs("AllocateAddress", HEADERS, ""), getCustomInputs(), getNetworkInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AllocateAddress")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testAttachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DeleteNetworkInterface", HEADERS, ""), getCustomInputs(), getNetworkInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DeleteNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testCreateNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("CreateNetworkInterface", HEADERS, ""), getNetworkInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("CreateNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDeleteNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("AttachNetworkInterface", HEADERS, ""), getCustomInputs(), getNetworkInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("AttachNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testDetachNetworkInterface() throws Exception {
        toTest.execute(getCommonInputs("DetachNetworkInterface", HEADERS, ""), getCustomInputs(), getNetworkInputs());

        verify(amazonSignatureServiceMock, times(1)).signRequestHeaders(any(InputsWrapper.class), eq(getHeadersMap()),
                eq(getQueryParamsMap("DetachNetworkInterface")));
        runCommonVerifiersForQueryApi();
    }

    @Test
    public void testExecuteException() throws Exception {
        MockingHelper.setExpectedExceptions(exception, RuntimeException.class, "Unsupported Query API.");

        toTest.execute(getCommonInputs("", "", ""), getCustomInputs(), getVolumeInputs(), getNetworkInputs());

        verify(amazonSignatureServiceMock, never()).signRequestHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        verify(csHttpClientMock, never()).execute(any(HttpClientInputs.class));
    }

    private void addCommonMocksForQueryApi() throws Exception {
        whenNew(AmazonSignatureService.class).withNoArguments().thenReturn(amazonSignatureServiceMock);
        when(amazonSignatureServiceMock.signRequestHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class))).thenReturn(authorizationHeaderMock);
        doNothing().when(queryApiExecutorSpy).setQueryApiHeaders(any(InputsWrapper.class),
                anyMapOf(String.class, String.class), anyMapOf(String.class, String.class));
        whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        when(csHttpClientMock.execute(any(HttpClientInputs.class))).thenReturn(null);
    }

    private void runCommonVerifiersForQueryApi() throws Exception {
        verifyNew(AmazonSignatureService.class).withNoArguments();
        verifyNew(CSHttpClient.class).withNoArguments();
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));
        verifyNoMoreInteractions(amazonSignatureServiceMock);
        verifyNoMoreInteractions(csHttpClientMock);
    }

    private CommonInputs getCommonInputs(String action, String headersString, String queryParamsString) throws MalformedURLException {
        return new CommonInputs.CommonInputsBuilder()
                .withAction(action)
                .withHeaders(headersString)
                .withQueryParams(queryParamsString)
                .withVersion("2016-04-01")
                .withDelimiter(",")
                .build();
    }

    private CustomInputs getCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder()
                .withAvailabilityZone("us-east-1d")
                .withInstanceId("i-abcdef12")
                .withAttachmentId("eni-attach-12345678")
                .withVolumeType("")
                .withDomain("")
                .build();
    }

    private VolumeInputs getVolumeInputs() {
        return new VolumeInputs.VolumeInputsBuilder().withSize("10").withIops("").build();
    }

    private NetworkInputs getNetworkInputs() {
        return new NetworkInputs.NetworkInputsBuilder()
                .withNetworkInterfaceId("eni-12345678")
                .withDeviceIndex("25")
                .withNetworkInterfaceDescription("anything in here")
                .withNetworkInterfacePrivateIpAddress("10.0.0.129")
                .withNetworkInterfaceSubnetId("subnet-abcdef12")
                .withPrivateIpAddressesString("10.0.0.130,10.0.0.131,10.0.0.132")
                .withSecondaryPrivateIpAddressCount("3")
                .withSecurityGroupIdsString("sg-12345678,sg-abcdef12")
                .withNetworkInterfaceSubnetId("subnet-abcdef12")
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

        switch (action) {
            case "CreateVolume":
                queryParamsMap.put("VolumeType", "standard");
                queryParamsMap.put("Size", "10");
                queryParamsMap.put("AvailabilityZone", "us-east-1d");
                break;
            case "AllocateAddress":
                queryParamsMap.put("Domain", "standard");
                break;
            case "AttachNetworkInterface":
                queryParamsMap.put("InstanceId", "i-abcdef12");
                queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
                queryParamsMap.put("DeviceIndex", "25");
                break;
            case "CreateNetworkInterface":
                queryParamsMap.put("SubnetId", "subnet-abcdef12");
                queryParamsMap.put("Description", "anything in here");
                queryParamsMap.put("PrivateIpAddresses.1.Primary", "true");
                queryParamsMap.put("PrivateIpAddresses.1.PrivateIpAddress", "10.0.0.129");
                queryParamsMap.put("PrivateIpAddresses.2.Primary", "false");
                queryParamsMap.put("PrivateIpAddresses.2.PrivateIpAddress", "10.0.0.130");
                queryParamsMap.put("PrivateIpAddresses.3.Primary", "false");
                queryParamsMap.put("PrivateIpAddresses.3.PrivateIpAddress", "10.0.0.131");
                queryParamsMap.put("PrivateIpAddresses.4.Primary", "false");
                queryParamsMap.put("PrivateIpAddresses.4.PrivateIpAddress", "10.0.0.132");
                queryParamsMap.put("SecurityGroupId.1", "sg-12345678");
                queryParamsMap.put("SecurityGroupId.2", "sg-abcdef12");
                break;
            case "DeleteNetworkInterface":
                queryParamsMap.put("NetworkInterfaceId", "eni-12345678");
                break;
            case "DetachNetworkInterface":
                queryParamsMap.put("AttachmentId", "eni-attach-12345678");
                break;
            default:
                throw new RuntimeException("You forgot to setup queryParamsMap naughty developer!");
        }

        return queryParamsMap;
    }
}