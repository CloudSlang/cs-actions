package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AWSApiVolumeService;
import io.cloudslang.content.jclouds.services.AWSApiBaseService;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public class AWSApiVolumeServiceImpl implements AWSApiVolumeService {
    public Map<String, String> createVolume(AWSInputsWrapper inputs) throws MalformedURLException, SignatureException {
        AWSApiBaseService helper = new AWSApiBaseService();
        Map<String, String> headersMap = helper.getNullOrHeadersMap(null, inputs);
        Map<String, String> queryParamsMap = helper.getApiQueryParamsMap(inputs, AWSApiAction.CREATE_VOLUME.getValue());

        helper.setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }
}