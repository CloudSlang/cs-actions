package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.jclouds.entities.aws.HttpClientMethod;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
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
 * 8/19/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AWSApiVolumeServiceImpl.class, AWSApiService.class, CSHttpClient.class})
public class AWSApiVolumeServiceTest {
    private AWSApiVolumeServiceImpl service;
    private AWSApiService awsApiServiceSpy;
    private Map<String, String> response;

    @Mock
    private CSHttpClient csHttpClientMock;

    @Before
    public void init() throws Exception {
        service = new AWSApiVolumeServiceImpl();
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

        AWSInputsWrapper wrapper = getCreateVolumeWrapper();
        Map<String, String> headersMap = getCreateVolumeEntriesMap(false, true);
        Map<String, String> queryParamsMap = getCreateVolumeEntriesMap(true, false);

        response = service.createVolume(wrapper);

        verify(awsApiServiceSpy, times(1)).getNullOrHeadersMap(eq((HashMap<String, String>) null), eq(wrapper));
        verify(awsApiServiceSpy, times(1)).getApiQueryParamsMap(eq(wrapper), eq("CreateVolume"));
        verify(awsApiServiceSpy, times(1))
                .setQueryApiCallHeaders(eq(wrapper), eq(headersMap), eq(queryParamsMap));
        verifyNoMoreInteractions(awsApiServiceSpy);
        verify(csHttpClientMock, times(1)).execute(any(HttpClientInputs.class));
        verifyNoMoreInteractions(csHttpClientMock);
    }

    private CommonInputs getCommonInputs() throws MalformedURLException {
        return new CommonInputs.CommonInputsBuilder()
                .withIdentity("AKIAIOSFODNN7EXAMPLE")
                .withCredential("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
                .withEndpoint("https://ec2.amazonaws.com")
                .build();
    }

    private CustomInputs getCreateVolumeCustomInputs() throws Exception {
        return new CustomInputs.CustomInputsBuilder()
                .withAvailabilityZone("us-east-1d")
                .withVolumeType("gp2")
                .withKmsKeyId("arn:aws:kms:us-east-1:012345678910:key/abcd1234-a123-456a-a12b-a123b4cd56ef")
                .build();
    }

    private VolumeInputs getVolumeInputs() {
        return new VolumeInputs.VolumeInputsBuilder()
                .withEncrypted("true")
                .withIops("300")
                .withSize("10")
                .withSnapshotId("snap-abcdef12")
                .build();
    }

    private AWSInputsWrapper getCreateVolumeWrapper() throws Exception {
        return new AWSInputsWrapper.AWSInputsWrapperBuilder()
                .withCommonInputs(getCommonInputs())
                .withCustomInputs(getCreateVolumeCustomInputs())
                .withHttpClientInputs(MockingHelper.getHttpClientInputs(true))
                .withVolumeInputs(getVolumeInputs())
                .withVersion("2014-06-15")
                .withApiService(Constants.Apis.AMAZON_EC2_API)
                .withRequestUri(Constants.Miscellaneous.EMPTY)
                .withRequestPayload(Constants.Miscellaneous.EMPTY)
                .withHttpVerb(HttpClientMethod.GET.toString())
                .build();
    }

    private Map<String, String> getCreateVolumeEntriesMap(boolean withQueryParams, boolean withHeaders) {
        Map<String, String> entriesMap = new HashMap<>();
        if (!withQueryParams && withHeaders) {
            entriesMap.put("cache-control", "no-cache");
            entriesMap.put("accept-charset", "UTF-8");
            entriesMap.put("accept", "application/xml");

            return entriesMap;
        }
        entriesMap.put("Action", "CreateVolume");
        entriesMap.put("Version", "2014-06-15");
        entriesMap.put("AvailabilityZone", "us-east-1d");
        entriesMap.put("KmsKeyId", "arn:aws:kms:us-east-1:012345678910:key/abcd1234-a123-456a-a12b-a123b4cd56ef");
        entriesMap.put("Size", "10");
        entriesMap.put("SnapshotId", "snap-abcdef12");
        entriesMap.put("Encrypted", "1");
        entriesMap.put("Iops", "300");
        entriesMap.put("VolumeType", "gp2");

        return entriesMap;
    }
}