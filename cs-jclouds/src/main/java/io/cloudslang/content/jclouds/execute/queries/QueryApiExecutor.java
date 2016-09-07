package io.cloudslang.content.jclouds.execute.queries;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.jclouds.entities.aws.AuthorizationHeader;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.*;
import io.cloudslang.content.jclouds.factory.AwsApiInputsWrapperFactory;
import io.cloudslang.content.jclouds.factory.QueryApiParamsMapFactory;
import io.cloudslang.content.jclouds.services.AmazonSignatureService;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/6/2016.
 */
public class QueryApiExecutor {
    public Map<String, String> execute(CommonInputs commonInputs, CustomInputs customInputs, VolumeInputs volumeInputs,
                                       NetworkInputs networkInputs, String action) throws MalformedURLException, SignatureException {
        Map<String, String> headersMap = StringUtils.isNotBlank(commonInputs.getHeaders()) ?
                InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(), commonInputs.getHeaders(),
                        Constants.AwsParams.HEADER_DELIMITER, Constants.Miscellaneous.COLON, true) : new HashMap<String, String>();

        AwsInputsWrapper inputs = AwsApiInputsWrapperFactory.getWrapper(commonInputs, customInputs, volumeInputs, networkInputs, action);

        Map<String, String> queryParamsMap = QueryApiParamsMapFactory.getQueryApiParamsMap(inputs);

        setQueryApiCallParams(inputs, queryParamsMap);
        setQueryApiCallHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }

    private void setQueryApiCallParams(AwsInputsWrapper inputs, Map<String, String> queryParamsMap) {
        String queryParamsString = InputsUtil.getParamsString(queryParamsMap, Constants.Miscellaneous.EQUAL, Constants.Miscellaneous.AMPERSAND);
        inputs.getHttpClientInputs().setQueryParams(queryParamsString);
    }

    private void setQueryApiCallHeaders(AwsInputsWrapper inputs, Map<String, String> headersMap, Map<String, String> queryParamsMap)
            throws SignatureException, MalformedURLException {
        AuthorizationHeader signedHeaders = new AmazonSignatureService().signRequestHeaders(inputs, headersMap, queryParamsMap);
        inputs.getHttpClientInputs().setHeaders(signedHeaders.getAuthorizationHeader());
    }
}