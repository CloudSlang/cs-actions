package io.cloudslang.content.jclouds.services;

import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.services.helpers.AWSApiNetworkServiceHelper;
import io.cloudslang.content.jclouds.services.helpers.AWSApiVolumeServiceHelper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/16/2016.
 */
public class AWSApiService {
    public Map<String, String> getApiQueryParamsMap(AWSInputsWrapper inputs, String actionName) {
        Map<String, String> queryParamsMap = new HashMap<>();
        if (StringUtils.isBlank(inputs.getHttpClientInputs().getQueryParams())) {
            if (AWSApiAction.ATTACH_NETWORK_INTERFACE.getValue().equalsIgnoreCase(actionName)) {
                queryParamsMap = new AWSApiNetworkServiceHelper().getAttachNetworkInterfaceQueryParamsMap(inputs);
            } else if (AWSApiAction.DETACH_NETWORK_INTERFACE.getValue().equalsIgnoreCase(actionName)) {
                queryParamsMap = new AWSApiNetworkServiceHelper().getDetachNetworkInterfaceQueryParamsMap(inputs);
            } else if (AWSApiAction.CREATE_VOLUME.getValue().equalsIgnoreCase(actionName)) {
                queryParamsMap = new AWSApiVolumeServiceHelper().getCreateVolumeQueryParamsMap(inputs);
            }

            String queryParamsString = queryParamsMap.isEmpty() ? Constants.Miscellaneous.EMPTY :
                    InputsUtil.getParamsString(queryParamsMap, Constants.Miscellaneous.EQUAL, Constants.Miscellaneous.AMPERSAND);

            inputs.getHttpClientInputs().setQueryParams(queryParamsString);
        } else {
            queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(),
                    inputs.getHttpClientInputs().getQueryParams(), Constants.Miscellaneous.AMPERSAND,
                    Constants.Miscellaneous.EQUAL, false);
        }
        return queryParamsMap;
    }

    public Map<String, String> getNullOrHeadersMap(Map<String, String> headersMap, AWSInputsWrapper inputs) {
        if (headersMap == null || headersMap.isEmpty()) {
            headersMap = new HashMap<>();
        }
        return StringUtils.isBlank(inputs.getHttpClientInputs().getHeaders()) ? headersMap :
                InputsUtil.getHeadersOrQueryParamsMap(headersMap, inputs.getHttpClientInputs().getHeaders(),
                        Constants.AWSParams.HEADER_DELIMITER, Constants.Miscellaneous.COLON, true);
    }

    public void setQueryApiCallHeaders(AWSInputsWrapper inputs, Map<String, String> headersMap, Map<String, String> queryParamsMap)
            throws SignatureException, MalformedURLException {
        AuthorizationHeader signedHeaders = new AmazonSignatureService().signRequestHeaders(inputs, headersMap, queryParamsMap);

        inputs.getHttpClientInputs().setHeaders(signedHeaders.getAuthorizationHeader());
    }
}