package io.cloudslang.content.amazon.execute.queries;

import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.amazon.entities.aws.AuthorizationHeader;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.InputsWrapperBuilder;
import io.cloudslang.content.amazon.factory.ParamsMapBuilder;
import io.cloudslang.content.amazon.services.AmazonSignatureService;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.net.MalformedURLException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HEADER_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COLON;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;

/**
 * Created by Mihai Tusa.
 * 9/6/2016.
 */
public class QueryApiExecutor {
    @SafeVarargs
    public final <T> Map<String, String> execute(CommonInputs commonInputs, T... builders) throws Exception {
        InputsWrapper inputs = InputsWrapperBuilder.getWrapper(commonInputs, builders);
        Map<String, String> queryParamsMap = ParamsMapBuilder.getParamsMap(inputs);

        Map<String, String> headersMap = isBlank(inputs.getCommonInputs().getHeaders()) ? new HashMap<String, String>() :
                InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(), inputs.getCommonInputs().getHeaders(),
                        HEADER_DELIMITER, COLON, true);

        setQueryApiParams(inputs, queryParamsMap);
        setQueryApiHeaders(inputs, headersMap, queryParamsMap);

        return new CSHttpClient().execute(inputs.getHttpClientInputs());
    }

    void setQueryApiHeaders(InputsWrapper inputs, Map<String, String> headersMap, Map<String, String> queryParamsMap)
            throws SignatureException, MalformedURLException {
        AuthorizationHeader signedHeaders = new AmazonSignatureService().signRequestHeaders(inputs, headersMap, queryParamsMap);
        inputs.getHttpClientInputs().setHeaders(signedHeaders.getAuthorizationHeader());
    }

    private void setQueryApiParams(InputsWrapper inputs, Map<String, String> queryParamsMap) {
        String queryParamsString = InputsUtil.getParamsString(queryParamsMap, EQUAL, AMPERSAND);
        inputs.getHttpClientInputs().setQueryParams(queryParamsString);
    }
}