package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AWSApiVolumeService;
import io.cloudslang.content.jclouds.services.AWSApiService;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public class AWSApiVolumeServiceImpl implements AWSApiVolumeService {
    public Map<String, String> createVolume(AWSInputsWrapper inputs) throws MalformedURLException, SignatureException {
        AWSApiService service = new AWSApiService();
        Map<String, String> headersMap = service.getNullOrHeadersMap(null, inputs);
        Map<String, String> queryParamsMap = service.getApiQueryParamsMap(inputs, AWSApiAction.CREATE_VOLUME.getValue());

        service.setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }
}