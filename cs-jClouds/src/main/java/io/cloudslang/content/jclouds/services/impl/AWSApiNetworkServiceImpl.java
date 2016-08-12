package io.cloudslang.content.jclouds.services.impl;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AWSApiNetworkService;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.services.helpers.AWSApiNetworkServiceHelper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/9/2016.
 */
public class AWSApiNetworkServiceImpl implements AWSApiNetworkService {
    public Map<String, String> attachNetworkInterface(AWSInputsWrapper inputs) throws SignatureException, MalformedURLException {
        Map<String, String> headersMap = StringUtils.isBlank(inputs.getHttpClientInputs().getHeaders()) ? null :
                InputsUtil.getHeadersMap(inputs.getHttpClientInputs().getHeaders());

        Map<String, String> queryParamsMap;
        if (StringUtils.isBlank(inputs.getHttpClientInputs().getQueryParams())) {
            queryParamsMap = new AWSApiNetworkServiceHelper().getAttachNetworkInterfaceQueryParamsMap(inputs);
            String queryParamsString = InputsUtil
                    .getParamsString(queryParamsMap, Constants.Miscellaneous.EQUAL, Constants.Miscellaneous.AMPERSAND);
            inputs.getHttpClientInputs().setQueryParams(queryParamsString);
        } else {
            queryParamsMap = InputsUtil.getQueryParamsMap(inputs.getHttpClientInputs().getQueryParams());
        }

        AuthorizationHeader signedHeaders = new AmazonSignatureService().signRequestHeaders(inputs.getServiceEndpoint(),
                inputs.getCommonInputs().getIdentity(), inputs.getCommonInputs().getCredential(), inputs.getApiService(),
                inputs.getRequestUri(), inputs.getHttpClientInputs().getMethod(), inputs.getRequestPayload(),
                inputs.getSecurityToken(), inputs.getDate(), headersMap, queryParamsMap);

        inputs.getHttpClientInputs().setHeaders(signedHeaders.getAuthorizationHeader());

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }
}