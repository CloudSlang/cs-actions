package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.HttpClientMethod;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.services.AWSApiService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Mihai Tusa.
 * 8/18/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AWSApiNetworkServiceImpl.class, AWSApiService.class, CSHttpClient.class})
public class AWSApiNetworkServiceImplTest {
    private AWSApiNetworkServiceImpl service;
    private AWSApiService awsApiServiceSpy;
    private Map<String, String> response;

    @Mock
    private CSHttpClient csHttpClientMock;

    @Before
    public void init() throws Exception {
        service = new AWSApiNetworkServiceImpl();
        awsApiServiceSpy = spy(new AWSApiService());
        response = new HashMap<>();
    }

    @After
    public void tearDown() {
        service = null;
        awsApiServiceSpy = null;
        response = null;
    }

    @Test
    public void attachNetworkInterface() throws Exception {
        MockingHelper.addCommonMocksForQueryApiMethods(awsApiServiceSpy, csHttpClientMock, response);

        AWSInputsWrapper wrapper = getAttachNetworkInterfaceWrapper();
        Map<String, String> headersMap = getAttachNetworkInterfaceEntriesMap(true);
        Map<String, String> queryParamsMap = getAttachNetworkInterfaceEntriesMap(false);

        response = service.attachNetworkInterface(wrapper);

        verify(awsApiServiceSpy, times(1)).getNullOrHeadersMap(eq((HashMap<String, String>) null), eq(wrapper));
        verify(awsApiServiceSpy, times(1)).getApiQueryParamsMap(eq(wrapper), eq("AttachNetworkInterface"));
        verify(awsApiServiceSpy, times(1))
                .setQueryApiCallHeaders(eq(wrapper), eq(headersMap), eq(queryParamsMap));
        verifyNoMoreInteractions(awsApiServiceSpy);
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));
        verifyNoMoreInteractions(csHttpClientMock);
    }

    @Test
    public void detachNetworkInterface() throws Exception {
        MockingHelper.addCommonMocksForQueryApiMethods(awsApiServiceSpy, csHttpClientMock, response);

        AWSInputsWrapper wrapper = getDetachNetworkInterfaceWrapper();
        Map<String, String> headersMap = getDetachNetworkInterfaceEntriesMap(true);
        Map<String, String> queryParamsMap = getDetachNetworkInterfaceEntriesMap(false);

        response = service.detachNetworkInterface(wrapper);

        verify(awsApiServiceSpy, times(1)).getNullOrHeadersMap(eq((HashMap<String, String>) null), eq(wrapper));
        verify(awsApiServiceSpy, times(1)).getApiQueryParamsMap(eq(wrapper), eq("DetachNetworkInterface"));
        verify(awsApiServiceSpy, times(1))
                .setQueryApiCallHeaders(eq(wrapper), eq(headersMap), eq(queryParamsMap));
        verifyNoMoreInteractions(awsApiServiceSpy);
    }

    private CommonInputs getCommonInputs() throws MalformedURLException {
        return new CommonInputs.CommonInputsBuilder()
                .withIdentity("AKIAIOSFODNN7EXAMPLE")
                .withCredential("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                .withEndpoint("https://ec2.amazonaws.com")
                .build();
    }

    private CustomInputs getAttachNetworkInterfaceCustomInputs() {
        return new CustomInputs.CustomInputsBuilder().withInstanceId("i-abcdef12").build();
    }

    private AWSInputsWrapper getAttachNetworkInterfaceWrapper() throws MalformedURLException {
        return new AWSInputsWrapper.AWSInputsWrapperBuilder()
                .withCommonInputs(getCommonInputs())
                .withCustomInputs(getAttachNetworkInterfaceCustomInputs())
                .withHttpClientInputs(MockingHelper.getHttpClientInputs(false))
                .withNetworkInterfaceId("eni-12345678")
                .withDeviceIndex("1")
                .withVersion("2014-06-15")
                .withApiService(Constants.Apis.AMAZON_EC2_API)
                .withRequestUri(Constants.Miscellaneous.EMPTY)
                .withRequestPayload(Constants.Miscellaneous.EMPTY)
                .withHttpVerb(HttpClientMethod.GET.toString())
                .build();
    }

    private Map<String, String> getAttachNetworkInterfaceEntriesMap(boolean empty) {
        Map<String, String> entriesMap = new HashMap<>();
        if (empty) {
            return entriesMap;
        }
        entriesMap.put("Action", "AttachNetworkInterface");
        entriesMap.put("Version", "2014-06-15");
        entriesMap.put("InstanceId", "i-abcdef12");
        entriesMap.put("DeviceIndex", "1");
        entriesMap.put("NetworkInterfaceId", "eni-12345678");

        return entriesMap;
    }

    private AWSInputsWrapper getDetachNetworkInterfaceWrapper() throws MalformedURLException {
        return new AWSInputsWrapper.AWSInputsWrapperBuilder()
                .withCommonInputs(getCommonInputs())
                .withHttpClientInputs(MockingHelper.getHttpClientInputs(false))
                .withAttachmentId("eni-attach-12345678")
                .withVersion("2014-06-15")
                .withForceDetach("true")
                .withApiService(Constants.Apis.AMAZON_EC2_API)
                .withRequestUri(Constants.Miscellaneous.EMPTY)
                .withRequestPayload(Constants.Miscellaneous.EMPTY)
                .withHttpVerb(HttpClientMethod.GET.toString())
                .build();
    }

    private Map<String, String> getDetachNetworkInterfaceEntriesMap(boolean empty) {
        Map<String, String> entriesMap = new HashMap<>();
        if (empty) {
            return entriesMap;
        }
        entriesMap.put("Action", "DetachNetworkInterface");
        entriesMap.put("Version", "2014-06-15");
        entriesMap.put("AttachmentId", "eni-attach-12345678");
        entriesMap.put("Force", "1");

        return entriesMap;
    }
}