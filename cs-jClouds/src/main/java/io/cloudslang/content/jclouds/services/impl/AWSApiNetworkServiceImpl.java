package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AWSApiNetworkService;
import io.cloudslang.content.jclouds.services.AWSApiService;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/9/2016.
 */
public class AWSApiNetworkServiceImpl implements AWSApiNetworkService {
    public Map<String, String> attachNetworkInterface(AWSInputsWrapper inputs) throws SignatureException, MalformedURLException {
        AWSApiService service = new AWSApiService();
        Map<String, String> headersMap = service.getNullOrHeadersMap(null, inputs);
        Map<String, String> queryParamsMap = service.getApiQueryParamsMap(inputs, AWSApiAction.ATTACH_NETWORK_INTERFACE.getValue());

        service.setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }

    public Map<String, String> detachNetworkInterface(AWSInputsWrapper inputs) throws MalformedURLException, SignatureException {
        AWSApiService service = new AWSApiService();
        Map<String, String> headersMap = service.getNullOrHeadersMap(null, inputs);
        Map<String, String> queryParamsMap = service.getApiQueryParamsMap(inputs, AWSApiAction.DETACH_NETWORK_INTERFACE.getValue());

        service.setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }
}