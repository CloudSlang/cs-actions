package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AWSApiNetworkService;
import io.cloudslang.content.jclouds.services.AWSApiBaseService;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/9/2016.
 */
public class AWSApiNetworkServiceImpl implements AWSApiNetworkService {
    public Map<String, String> attachNetworkInterface(AWSInputsWrapper inputs) throws SignatureException, MalformedURLException {
        AWSApiBaseService helper = new AWSApiBaseService();
        Map<String, String> headersMap = helper.getNullOrHeadersMap(null, inputs);
        Map<String, String> queryParamsMap = helper.getApiQueryParamsMap(inputs, AWSApiAction.ATTACH_NETWORK_INTERFACE.getValue());

        helper.setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }

    public Map<String, String> detachNetworkInterface(AWSInputsWrapper inputs) throws MalformedURLException, SignatureException {
        AWSApiBaseService helper = new AWSApiBaseService();
        Map<String, String> headersMap = helper.getNullOrHeadersMap(null, inputs);
        Map<String, String> queryParamsMap = helper.getApiQueryParamsMap(inputs, AWSApiAction.DETACH_NETWORK_INTERFACE.getValue());

        helper.setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }
}